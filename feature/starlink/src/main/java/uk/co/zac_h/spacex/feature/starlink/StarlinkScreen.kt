package uk.co.zac_h.spacex.feature.starlink

import android.opengl.GLSurfaceView
import android.text.format.DateUtils
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import uk.co.zac_h.spacex.feature.starlink.globe.GlobeCamera
import uk.co.zac_h.spacex.feature.starlink.globe.GlobeRenderer
import uk.co.zac_h.spacex.feature.starlink.globe.SatelliteFrame
import uk.co.zac_h.spacex.feature.starlink.orbit.SatelliteFix
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CelestrakRateLimitedException
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/** An interactive globe with every Starlink satellite where it is right now. */
@Composable
fun StarlinkScreen(
    modifier: Modifier = Modifier,
    viewModel: StarlinkViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tapRadius = with(LocalDensity.current) { 24.dp.toPx() }

    Box(modifier = modifier.fillMaxSize()) {
        Globe(
            modifier = Modifier.fillMaxSize(),
            camera = viewModel.camera,
            frames = viewModel.frames,
            onTap = { offset, width, height -> viewModel.onTap(offset, width, height, tapRadius) }
        )

        StarlinkHeader(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp),
            uiState = uiState,
            frames = viewModel.frames,
            onRetry = { viewModel.refresh(force = true) }
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
                .widthIn(max = 480.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SelectedSatellite(frames = viewModel.frames, onClose = viewModel::clearSelection)
            SpeedControls(
                speed = uiState.speed,
                isLive = uiState.isLive,
                onSpeed = viewModel::setSpeed,
                onLive = viewModel::goLive
            )
        }

        if (uiState.satelliteCount == 0) {
            EmptyState(
                modifier = Modifier.align(Alignment.Center),
                refresh = uiState.refresh,
                onRetry = { viewModel.refresh(force = true) }
            )
        }
    }
}

@Composable
private fun Globe(
    modifier: Modifier,
    camera: GlobeCamera,
    frames: StateFlow<SatelliteFrame?>,
    onTap: (Offset, Int, Int) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val renderer = remember { GlobeRenderer(context.resources, R.drawable.starlink_earth, camera) }
    val view = remember {
        GLSurfaceView(context).apply {
            setEGLContextClientVersion(2)
            setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            preserveEGLContextOnPause = true
            setRenderer(renderer)
            // Drawn only when the satellites move or the camera turns, not 60 times a second.
            renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        }
    }

    DisposableEffect(lifecycleOwner, view) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> view.onResume()
                Lifecycle.Event.ON_PAUSE -> view.onPause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            view.onPause()
        }
    }

    LaunchedEffect(frames, view) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            frames.collect { frame ->
                renderer.frame = frame
                view.requestRender()
            }
        }
    }

    Box(modifier = modifier) {
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { view })
        // Gestures are read here, over the view, so the GLSurfaceView never has to handle touch.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(camera) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        camera.drag(pan.x, pan.y, size.height)
                        camera.zoom(zoom)
                        view.requestRender()
                    }
                }
                .pointerInput(camera) {
                    detectTapGestures { offset -> onTap(offset, size.width, size.height) }
                }
        )
    }
}

@Composable
private fun StarlinkHeader(
    modifier: Modifier,
    uiState: StarlinkUiState,
    frames: StateFlow<SatelliteFrame?>,
    onRetry: () -> Unit
) {
    val now by produceState(System.currentTimeMillis()) {
        while (true) {
            delay(30_000)
            value = System.currentTimeMillis()
        }
    }
    // Whole seconds only, so this recomposes once a second rather than with every frame.
    val simulatedSecond by remember(frames) {
        frames.map { it?.timeMillis?.div(1000) }.distinctUntilChanged()
    }.collectAsStateWithLifecycle(null)

    OverlaySurface(modifier = modifier) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(text = stringResource(R.string.starlink_title), style = MaterialTheme.typography.titleMedium)
            if (uiState.satelliteCount > 0) {
                Text(
                    text = pluralStringResource(
                        R.plurals.starlink_satellite_count,
                        uiState.satelliteCount,
                        uiState.satelliteCount
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            simulatedSecond?.let { seconds ->
                Text(
                    text = stringResource(R.string.starlink_simulated_time, TIME_FORMAT.format(Instant.ofEpochSecond(seconds))),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            val error = (uiState.refresh as? ApiResult.Failure)?.exception
            when {
                error is CelestrakRateLimitedException && uiState.satelliteCount > 0 -> Text(
                    text = rateLimitedText(error),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                error != null && uiState.satelliteCount > 0 -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.starlink_update_failed),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    TextButton(onClick = onRetry) { Text(stringResource(R.string.starlink_retry)) }
                }

                uiState.fetchedAt != null -> Text(
                    text = stringResource(
                        R.string.starlink_updated,
                        DateUtils.getRelativeTimeSpanString(
                            uiState.fetchedAt, now.coerceAtLeast(uiState.fetchedAt), DateUtils.MINUTE_IN_MILLIS
                        )
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SelectedSatellite(frames: StateFlow<SatelliteFrame?>, onClose: () -> Unit) {
    val details by remember(frames) { frames.map { it?.details } }.collectAsStateWithLifecycle(null)
    val fix = details ?: return
    OverlaySurface(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = fix.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = stringResource(R.string.starlink_norad_id, fix.noradId),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(R.string.starlink_close))
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            SatelliteStats(fix)
        }
    }
}

@Composable
private fun SatelliteStats(fix: SatelliteFix) {
    Row(modifier = Modifier.fillMaxWidth().padding(end = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Stat(
            label = stringResource(R.string.starlink_altitude),
            value = stringResource(R.string.starlink_altitude_value, fix.position.altitudeKm)
        )
        Stat(
            label = stringResource(R.string.starlink_speed_label),
            value = stringResource(R.string.starlink_speed_value, fix.speedKmPerSecond)
        )
        Stat(
            label = stringResource(R.string.starlink_position),
            value = stringResource(R.string.starlink_position_value, fix.position.latitude, fix.position.longitude)
        )
    }
}

@Composable
private fun Stat(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpeedControls(speed: Int, isLive: Boolean, onSpeed: (Int) -> Unit, onLive: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.weight(1f)) {
            SimulationClock.SPEEDS.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = speed == option,
                    onClick = { onSpeed(option) },
                    shape = SegmentedButtonDefaults.itemShape(index, SimulationClock.SPEEDS.size)
                ) {
                    Text(stringResource(R.string.starlink_speed, option))
                }
            }
        }
        Button(onClick = onLive, enabled = !isLive) {
            Text(stringResource(R.string.starlink_live))
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier, refresh: ApiResult<Unit>, onRetry: () -> Unit) {
    OverlaySurface(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val error = (refresh as? ApiResult.Failure)?.exception
            if (error is CelestrakRateLimitedException) {
                // Retrying early can't work and CelesTrak blocks IPs that keep asking, so no button.
                Text(stringResource(R.string.starlink_load_failed))
                Text(text = rateLimitedText(error), style = MaterialTheme.typography.bodyMedium)
            } else if (error != null) {
                Text(stringResource(R.string.starlink_load_failed))
                Button(onClick = onRetry) { Text(stringResource(R.string.starlink_retry)) }
            } else {
                CircularProgressIndicator()
                Text(stringResource(R.string.starlink_loading))
            }
        }
    }
}

@Composable
private fun rateLimitedText(error: CelestrakRateLimitedException): String {
    val context = LocalContext.current
    return stringResource(
        R.string.starlink_rate_limited,
        DateUtils.formatDateTime(context, error.retryAtMillis, DateUtils.FORMAT_SHOW_TIME)
    )
}

@Composable
private fun OverlaySurface(modifier: Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.85f),
        content = content
    )
}

private val TIME_FORMAT: DateTimeFormatter =
    DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm:ss").withZone(ZoneOffset.UTC)
