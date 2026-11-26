package uk.co.zac_h.spacex.feature.starlink.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import uk.co.zac_h.spacex.feature.starlink.R
import uk.co.zac_h.spacex.feature.starlink.orbit.TrackPoint

/**
 * A flat (equirectangular) world map with the satellite's ground track: the part it has flown
 * faded, the part still to come bright, and a dot where it is now.
 */
@Composable
fun GroundTrackMap(track: List<TrackPoint>, modifier: Modifier = Modifier) {
    val earth = ImageBitmap.imageResource(R.drawable.starlink_earth)
    val shape = MaterialTheme.shapes.medium
    Canvas(
        modifier = modifier
            .aspectRatio(2f)
            .clip(shape)
    ) {
        drawImage(
            image = earth,
            srcOffset = IntOffset.Zero,
            srcSize = IntSize(earth.width, earth.height),
            dstSize = IntSize(size.width.toInt(), size.height.toInt())
        )
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawTrack(track.filter { it.minutesFromNow <= 0 }, PAST_COLOR, stroke)
        drawTrack(track.filter { it.minutesFromNow >= 0 }, FUTURE_COLOR, stroke)

        track.minByOrNull { abs(it.minutesFromNow) }?.let { now ->
            val centre = project(now)
            drawCircle(Color.White, radius = 6.dp.toPx(), center = centre)
            drawCircle(FUTURE_COLOR, radius = 4.dp.toPx(), center = centre)
        }
    }
}

private fun DrawScope.drawTrack(points: List<TrackPoint>, color: Color, stroke: Stroke) {
    if (points.size < 2) return
    val path = Path()
    points.forEachIndexed { index, point ->
        val offset = project(point)
        // Crossing the antimeridian: start a new line on the other edge rather than drawing
        // one across the whole map.
        if (index == 0 || abs(point.longitude - points[index - 1].longitude) > 180) {
            path.moveTo(offset.x, offset.y)
        } else {
            path.lineTo(offset.x, offset.y)
        }
    }
    drawPath(path, color, style = stroke)
}

internal fun DrawScope.project(point: TrackPoint): Offset = Offset(
    x = ((point.longitude + 180) / 360 * size.width).toFloat(),
    y = ((90 - point.latitude) / 180 * size.height).toFloat()
)

private val PAST_COLOR = Color(0x99FFFFFF)
private val FUTURE_COLOR = Color(0xFFFF9800)
