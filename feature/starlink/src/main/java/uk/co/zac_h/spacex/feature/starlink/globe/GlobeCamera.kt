package uk.co.zac_h.spacex.feature.starlink.globe

import android.opengl.Matrix
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * An orbit camera looking at the centre of the globe. [longitude] and [latitude] (radians) say
 * which point on the Earth is under the camera, and [distance] is in Earth radii from the centre.
 *
 * Gestures change it on the main thread while the renderer reads it on the GL thread, so each
 * value is volatile; a frame may mix an old and a new value, which is invisible.
 */
class GlobeCamera {

    @Volatile var longitude = 0f
        private set
    @Volatile var latitude = Math.toRadians(25.0).toFloat()
        private set
    @Volatile var distance = 3.2f
        private set

    /** Turns the globe by a drag of [dx], [dy] pixels on a view [viewHeight] pixels tall. */
    fun drag(dx: Float, dy: Float, viewHeight: Int) {
        if (viewHeight == 0) return
        // About one globe width per screen height when zoomed out, slower when zoomed in.
        val radiansPerPixel = 2.2f * (distance - 1f) / distance / viewHeight
        longitude -= dx * radiansPerPixel
        latitude = (latitude + dy * radiansPerPixel).coerceIn(-MAX_LATITUDE, MAX_LATITUDE)
    }

    /** Turns the globe so the point at [latitude], [longitude] (degrees) faces the camera. */
    fun lookAt(latitude: Double, longitude: Double) {
        this.longitude = Math.toRadians(longitude).toFloat()
        this.latitude = Math.toRadians(latitude).toFloat().coerceIn(-MAX_LATITUDE, MAX_LATITUDE)
    }

    fun zoom(factor: Float) {
        if (factor <= 0f) return
        distance = (distance / factor).coerceIn(MIN_DISTANCE, MAX_DISTANCE)
    }

    /** Camera position in the scene frame (same convention as a point at this lat/long). */
    fun eye(): FloatArray = floatArrayOf(
        distance * cos(latitude) * cos(longitude),
        distance * sin(latitude),
        -distance * cos(latitude) * sin(longitude)
    )

    /** The combined view-projection matrix for a [width] by [height] viewport. */
    fun viewProjection(width: Int, height: Int): FloatArray {
        val eye = eye()
        val view = FloatArray(16)
        Matrix.setLookAtM(view, 0, eye[0], eye[1], eye[2], 0f, 0f, 0f, 0f, 1f, 0f)
        val projection = FloatArray(16)
        val aspect = if (height == 0) 1f else width.toFloat() / height
        Matrix.perspectiveM(projection, 0, FIELD_OF_VIEW, aspect, 0.02f, 50f)
        val viewProjection = FloatArray(16)
        Matrix.multiplyMM(viewProjection, 0, projection, 0, view, 0)
        return viewProjection
    }

    /**
     * The index of the satellite drawn nearest to the tap at [x], [y] (pixels) within
     * [radiusPx], ignoring satellites hidden behind the Earth, or -1 if there is none.
     */
    fun pick(x: Float, y: Float, width: Int, height: Int, positions: FloatArray, radiusPx: Float): Int {
        if (width == 0 || height == 0) return -1
        val matrix = viewProjection(width, height)
        val eye = eye()
        val point = FloatArray(4)
        val clip = FloatArray(4)
        var best = -1
        var bestDistanceSq = radiusPx * radiusPx
        for (i in 0 until positions.size / 3) {
            val px = positions[i * 3]
            val py = positions[i * 3 + 1]
            val pz = positions[i * 3 + 2]
            if (px == 0f && py == 0f && pz == 0f) continue
            point[0] = px; point[1] = py; point[2] = pz; point[3] = 1f
            Matrix.multiplyMV(clip, 0, matrix, 0, point, 0)
            if (clip[3] <= 0f) continue
            val sx = (clip[0] / clip[3] + 1f) * 0.5f * width
            val sy = (1f - clip[1] / clip[3]) * 0.5f * height
            val distanceSq = (sx - x) * (sx - x) + (sy - y) * (sy - y)
            if (distanceSq < bestDistanceSq && isVisible(eye, px, py, pz)) {
                best = i
                bestDistanceSq = distanceSq
            }
        }
        return best
    }

    companion object {
        const val FIELD_OF_VIEW = 45f
        const val MIN_DISTANCE = 1.35f
        const val MAX_DISTANCE = 12f
        private val MAX_LATITUDE = Math.toRadians(85.0).toFloat()

        /** True if the line from [eye] to the point doesn't pass through the unit-sphere Earth. */
        internal fun isVisible(eye: FloatArray, px: Float, py: Float, pz: Float): Boolean {
            val dx = px - eye[0]
            val dy = py - eye[1]
            val dz = pz - eye[2]
            val a = dx * dx + dy * dy + dz * dz
            val b = 2f * (eye[0] * dx + eye[1] * dy + eye[2] * dz)
            val c = eye[0] * eye[0] + eye[1] * eye[1] + eye[2] * eye[2] - 1f
            val discriminant = b * b - 4f * a * c
            if (discriminant < 0f) return true
            val nearHit = (-b - sqrt(discriminant)) / (2f * a)
            return nearHit >= 1f || nearHit <= 0f
        }
    }
}
