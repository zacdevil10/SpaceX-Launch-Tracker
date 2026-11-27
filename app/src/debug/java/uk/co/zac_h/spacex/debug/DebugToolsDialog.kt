package uk.co.zac_h.spacex.debug

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.settings.SettingsDialogThemeChooserRow
import uk.co.zac_h.spacex.network.LaunchLibraryEnvironment
import kotlin.system.exitProcess

@Composable
fun DebugToolsDialog(
    onDismiss: () -> Unit,
    viewModel: DebugToolsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    DebugToolsDialog(
        state = state,
        onDismiss = onDismiss,
        onChangeLaunchLibraryEnvironment = { environment ->
            viewModel.setLaunchLibraryEnvironment(environment) { restartApp(context) }
        }
    )
}

/**
 * Each tool is a section here. To add one, add its setting to DebugSettings, its value to
 * [DebugToolsUiState], and a section below.
 */
@Composable
fun DebugToolsDialog(
    state: DebugToolsUiState?,
    onDismiss: () -> Unit,
    onChangeLaunchLibraryEnvironment: (LaunchLibraryEnvironment) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Debug tools") },
        text = {
            HorizontalDivider()
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (state != null) {
                    LaunchLibraryEnvironmentSection(
                        selected = state.launchLibraryEnvironment,
                        onChange = onChangeLaunchLibraryEnvironment
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK") }
        }
    )
}

@Composable
private fun LaunchLibraryEnvironmentSection(
    selected: LaunchLibraryEnvironment,
    onChange: (LaunchLibraryEnvironment) -> Unit
) {
    DebugToolsSectionTitle("Launch Library server")
    Text(
        text = "Switching clears the database and restarts the app.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Column(Modifier.selectableGroup()) {
        LaunchLibraryEnvironment.entries.forEach { environment ->
            SettingsDialogThemeChooserRow(
                text = environment.label,
                selected = environment == selected,
                onClick = { onChange(environment) }
            )
        }
    }
}

@Composable
private fun DebugToolsSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

/** Relaunches the app in a fresh process, so nothing from before the switch stays in memory. */
private fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName) ?: return
    context.startActivity(
        Intent.makeRestartActivityTask(intent.component)
    )
    exitProcess(0)
}

@ComponentPreviews
@Composable
private fun DebugToolsDialogPreview() {
    SpaceXTheme {
        DebugToolsDialog(
            state = DebugToolsUiState(launchLibraryEnvironment = LaunchLibraryEnvironment.DEVELOPMENT),
            onDismiss = {},
            onChangeLaunchLibraryEnvironment = {}
        )
    }
}
