package uk.co.zac_h.spacex.feature.starlink.globe

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.opengl.GLES20.GL_ARRAY_BUFFER
import android.opengl.GLES20.GL_BLEND
import android.opengl.GLES20.GL_CLAMP_TO_EDGE
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_COMPILE_STATUS
import android.opengl.GLES20.GL_CULL_FACE
import android.opengl.GLES20.GL_DEPTH_BUFFER_BIT
import android.opengl.GLES20.GL_DEPTH_TEST
import android.opengl.GLES20.GL_DYNAMIC_DRAW
import android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.GL_FRAGMENT_SHADER
import android.opengl.GLES20.GL_LINEAR
import android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR
import android.opengl.GLES20.GL_LINE_STRIP
import android.opengl.GLES20.GL_LINK_STATUS
import android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA
import android.opengl.GLES20.GL_POINTS
import android.opengl.GLES20.GL_REPEAT
import android.opengl.GLES20.GL_SRC_ALPHA
import android.opengl.GLES20.GL_STATIC_DRAW
import android.opengl.GLES20.GL_TEXTURE0
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.GL_TEXTURE_MAG_FILTER
import android.opengl.GLES20.GL_TEXTURE_MIN_FILTER
import android.opengl.GLES20.GL_TEXTURE_WRAP_S
import android.opengl.GLES20.GL_TEXTURE_WRAP_T
import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.GL_UNSIGNED_SHORT
import android.opengl.GLES20.GL_VERTEX_SHADER
import android.opengl.GLES20.glActiveTexture
import android.opengl.GLES20.glAttachShader
import android.opengl.GLES20.glBindBuffer
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glBlendFunc
import android.opengl.GLES20.glBufferData
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glCompileShader
import android.opengl.GLES20.glCreateProgram
import android.opengl.GLES20.glCreateShader
import android.opengl.GLES20.glDeleteShader
import android.opengl.GLES20.glDisable
import android.opengl.GLES20.glDisableVertexAttribArray
import android.opengl.GLES20.glDrawArrays
import android.opengl.GLES20.glDrawElements
import android.opengl.GLES20.glEnable
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glGenBuffers
import android.opengl.GLES20.glGenTextures
import android.opengl.GLES20.glGenerateMipmap
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetProgramInfoLog
import android.opengl.GLES20.glGetProgramiv
import android.opengl.GLES20.glGetShaderInfoLog
import android.opengl.GLES20.glGetShaderiv
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glLineWidth
import android.opengl.GLES20.glLinkProgram
import android.opengl.GLES20.glShaderSource
import android.opengl.GLES20.glTexParameteri
import android.opengl.GLES20.glUniform1f
import android.opengl.GLES20.glUniform1i
import android.opengl.GLES20.glUniform3f
import android.opengl.GLES20.glUniform4f
import android.opengl.GLES20.glUniformMatrix4fv
import android.opengl.GLES20.glUseProgram
import android.opengl.GLES20.glVertexAttribPointer
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Draws the Earth as a textured, sunlit unit sphere and every satellite as a point, with OpenGL ES
 * 2.0 (no extra libraries). Runs on the GLSurfaceView's thread; [frame] and [camera] are written
 * from other threads.
 */
class GlobeRenderer(
    private val resources: Resources,
    private val earthTexture: Int,
    private val camera: GlobeCamera
) : GLSurfaceView.Renderer {

    /** The latest satellites to draw. Set from any thread, then call requestRender(). */
    @Volatile
    var frame: SatelliteFrame? = null

    private val density = resources.displayMetrics.density
    private var width = 0
    private var height = 0

    private var earthProgram = 0
    private var pointProgram = 0
    private var lineProgram = 0
    private var earthVertices = 0
    private var earthIndices = 0
    private var earthIndexCount = 0
    private var texture = 0
    private var satelliteBuffer = 0
    private var orbitBuffer = 0
    private var uploaded: SatelliteFrame? = null

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.012f, 0.016f, 0.035f, 1f)
        glEnable(GL_DEPTH_TEST)
        earthProgram = program(EARTH_VERTEX, EARTH_FRAGMENT)
        pointProgram = program(POINT_VERTEX, POINT_FRAGMENT)
        lineProgram = program(POINT_VERTEX, LINE_FRAGMENT)
        createEarth()
        texture = loadTexture()
        val buffers = IntArray(2)
        glGenBuffers(2, buffers, 0)
        satelliteBuffer = buffers[0]
        orbitBuffer = buffers[1]
        // A new GL context has empty buffers, so the current frame must be uploaded again.
        uploaded = null
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        val matrix = camera.viewProjection(width, height)
        val frame = frame
        val sun = frame?.sun ?: DEFAULT_SUN

        drawEarth(matrix, sun)

        if (frame == null || frame.count == 0) return
        if (frame !== uploaded) {
            upload(satelliteBuffer, frame.positions)
            frame.orbit?.let { upload(orbitBuffer, it) }
            uploaded = frame
        }

        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        frame.orbit?.let { orbit ->
            glUseProgram(lineProgram)
            setMatrix(lineProgram, matrix)
            glUniform4f(glGetUniformLocation(lineProgram, "uColor"), 1f, 0.75f, 0.3f, 0.8f)
            glLineWidth(2f * density)
            drawVertices(lineProgram, orbitBuffer, GL_LINE_STRIP, 0, orbit.size / 3)
        }

        glUseProgram(pointProgram)
        setMatrix(pointProgram, matrix)
        // Points shrink as the camera moves away, but never below a pixel or so.
        val size = (density * 4.2f / camera.distance).coerceIn(density * 0.9f, density * 3.2f)
        glUniform1f(glGetUniformLocation(pointProgram, "uSize"), size)
        glUniform4f(glGetUniformLocation(pointProgram, "uColor"), 0.62f, 0.85f, 1f, 0.95f)
        drawVertices(pointProgram, satelliteBuffer, GL_POINTS, 0, frame.count)

        if (frame.selected in 0 until frame.count) {
            glDisable(GL_DEPTH_TEST)
            glUniform1f(glGetUniformLocation(pointProgram, "uSize"), size * 2.6f + density * 3f)
            glUniform4f(glGetUniformLocation(pointProgram, "uColor"), 1f, 0.75f, 0.3f, 1f)
            drawVertices(pointProgram, satelliteBuffer, GL_POINTS, frame.selected, 1)
            glEnable(GL_DEPTH_TEST)
        }
        glDisable(GL_BLEND)
    }


    private fun drawEarth(matrix: FloatArray, sun: FloatArray) {
        glEnable(GL_CULL_FACE)
        glUseProgram(earthProgram)
        setMatrix(earthProgram, matrix)
        glUniform3f(glGetUniformLocation(earthProgram, "uSun"), sun[0], sun[1], sun[2])
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texture)
        glUniform1i(glGetUniformLocation(earthProgram, "uTexture"), 0)

        val position = glGetAttribLocation(earthProgram, "aPosition")
        val uv = glGetAttribLocation(earthProgram, "aUv")
        glBindBuffer(GL_ARRAY_BUFFER, earthVertices)
        glEnableVertexAttribArray(position)
        glVertexAttribPointer(position, 3, GL_FLOAT, false, EARTH_STRIDE, 0)
        glEnableVertexAttribArray(uv)
        glVertexAttribPointer(uv, 2, GL_FLOAT, false, EARTH_STRIDE, 3 * FLOAT_BYTES)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, earthIndices)
        glDrawElements(GL_TRIANGLES, earthIndexCount, GL_UNSIGNED_SHORT, 0)
        glDisableVertexAttribArray(position)
        glDisableVertexAttribArray(uv)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        glDisable(GL_CULL_FACE)
    }

    private fun drawVertices(program: Int, buffer: Int, mode: Int, first: Int, count: Int) {
        val position = glGetAttribLocation(program, "aPosition")
        glBindBuffer(GL_ARRAY_BUFFER, buffer)
        glEnableVertexAttribArray(position)
        glVertexAttribPointer(position, 3, GL_FLOAT, false, 0, 0)
        glDrawArrays(mode, first, count)
        glDisableVertexAttribArray(position)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    private fun setMatrix(program: Int, matrix: FloatArray) {
        glUniformMatrix4fv(glGetUniformLocation(program, "uMatrix"), 1, false, matrix, 0)
    }

    private fun upload(buffer: Int, data: FloatArray) {
        glBindBuffer(GL_ARRAY_BUFFER, buffer)
        glBufferData(GL_ARRAY_BUFFER, data.size * FLOAT_BYTES, floatBuffer(data), GL_DYNAMIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    /** A UV sphere whose texture coordinates match an equirectangular map with 0° in the middle. */
    private fun createEarth() {
        val vertices = FloatArray((STACKS + 1) * (SLICES + 1) * 5)
        var v = 0
        for (stack in 0..STACKS) {
            val latitude = PI / 2 - PI * stack / STACKS
            for (slice in 0..SLICES) {
                val longitude = -PI + 2 * PI * slice / SLICES
                vertices[v++] = (cos(latitude) * cos(longitude)).toFloat()
                vertices[v++] = sin(latitude).toFloat()
                vertices[v++] = (-cos(latitude) * sin(longitude)).toFloat()
                vertices[v++] = slice.toFloat() / SLICES
                vertices[v++] = stack.toFloat() / STACKS
            }
        }
        val indices = ShortArray(STACKS * SLICES * 6)
        var i = 0
        for (stack in 0 until STACKS) {
            for (slice in 0 until SLICES) {
                val topLeft = (stack * (SLICES + 1) + slice).toShort()
                val bottomLeft = ((stack + 1) * (SLICES + 1) + slice).toShort()
                // Counter-clockwise seen from outside, so back faces can be culled.
                indices[i++] = topLeft
                indices[i++] = bottomLeft
                indices[i++] = (topLeft + 1).toShort()
                indices[i++] = (topLeft + 1).toShort()
                indices[i++] = bottomLeft
                indices[i++] = (bottomLeft + 1).toShort()
            }
        }
        earthIndexCount = indices.size

        val buffers = IntArray(2)
        glGenBuffers(2, buffers, 0)
        earthVertices = buffers[0]
        earthIndices = buffers[1]
        glBindBuffer(GL_ARRAY_BUFFER, earthVertices)
        glBufferData(GL_ARRAY_BUFFER, vertices.size * FLOAT_BYTES, floatBuffer(vertices), GL_STATIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        val indexBuffer: ShortBuffer = ByteBuffer.allocateDirect(indices.size * 2)
            .order(ByteOrder.nativeOrder()).asShortBuffer().put(indices).apply { position(0) }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, earthIndices)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.size * 2, indexBuffer, GL_STATIC_DRAW)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    private fun loadTexture(): Int {
        val ids = IntArray(1)
        glGenTextures(1, ids, 0)
        glBindTexture(GL_TEXTURE_2D, ids[0])
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        val bitmap = BitmapFactory.decodeResource(resources, earthTexture, BitmapFactory.Options().apply { inScaled = false })
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()
        glGenerateMipmap(GL_TEXTURE_2D)
        glBindTexture(GL_TEXTURE_2D, 0)
        return ids[0]
    }

    private fun program(vertexSource: String, fragmentSource: String): Int {
        val vertex = shader(GL_VERTEX_SHADER, vertexSource)
        val fragment = shader(GL_FRAGMENT_SHADER, fragmentSource)
        val program = glCreateProgram()
        glAttachShader(program, vertex)
        glAttachShader(program, fragment)
        glLinkProgram(program)
        val status = IntArray(1)
        glGetProgramiv(program, GL_LINK_STATUS, status, 0)
        check(status[0] != 0) { "Globe shader failed to link: ${glGetProgramInfoLog(program)}" }
        glDeleteShader(vertex)
        glDeleteShader(fragment)
        return program
    }

    private fun shader(type: Int, source: String): Int {
        val shader = glCreateShader(type)
        glShaderSource(shader, source)
        glCompileShader(shader)
        val status = IntArray(1)
        glGetShaderiv(shader, GL_COMPILE_STATUS, status, 0)
        check(status[0] != 0) { "Globe shader failed to compile: ${glGetShaderInfoLog(shader)}" }
        return shader
    }

    private fun floatBuffer(data: FloatArray): FloatBuffer =
        ByteBuffer.allocateDirect(data.size * FLOAT_BYTES).order(ByteOrder.nativeOrder())
            .asFloatBuffer().put(data).apply { position(0) }

    companion object {
        private const val STACKS = 64
        private const val SLICES = 128
        private const val FLOAT_BYTES = 4
        private const val EARTH_STRIDE = 5 * FLOAT_BYTES
        private val DEFAULT_SUN = floatArrayOf(1f, 0f, 0f)

        private const val EARTH_VERTEX = """
            uniform mat4 uMatrix;
            attribute vec3 aPosition;
            attribute vec2 aUv;
            varying vec2 vUv;
            varying vec3 vNormal;
            void main() {
                vUv = aUv;
                vNormal = aPosition;
                gl_Position = uMatrix * vec4(aPosition, 1.0);
            }
        """

        // Night side kept dim rather than black so the continents stay readable.
        private const val EARTH_FRAGMENT = """
            precision mediump float;
            uniform sampler2D uTexture;
            uniform vec3 uSun;
            varying vec2 vUv;
            varying vec3 vNormal;
            void main() {
                vec3 color = texture2D(uTexture, vUv).rgb;
                float daylight = smoothstep(-0.12, 0.22, dot(normalize(vNormal), uSun));
                gl_FragColor = vec4(color * (0.38 + 0.9 * daylight), 1.0);
            }
        """

        private const val POINT_VERTEX = """
            uniform mat4 uMatrix;
            uniform float uSize;
            attribute vec3 aPosition;
            void main() {
                gl_Position = uMatrix * vec4(aPosition, 1.0);
                gl_PointSize = uSize;
            }
        """

        private const val POINT_FRAGMENT = """
            precision mediump float;
            uniform vec4 uColor;
            void main() {
                vec2 p = gl_PointCoord - vec2(0.5);
                float d = dot(p, p);
                if (d > 0.25) discard;
                gl_FragColor = vec4(uColor.rgb, uColor.a * (1.0 - smoothstep(0.12, 0.25, d)));
            }
        """

        private const val LINE_FRAGMENT = """
            precision mediump float;
            uniform vec4 uColor;
            void main() {
                gl_FragColor = uColor;
            }
        """
    }
}
