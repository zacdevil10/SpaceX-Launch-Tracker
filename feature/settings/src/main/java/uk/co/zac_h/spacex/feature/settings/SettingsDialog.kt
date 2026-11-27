package uk.co.zac_h.spacex.feature.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.co.zac_h.spacex.core.ui.supportsDynamicTheming

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onOpenDebugTools: (() -> Unit)? = null,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val theme by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsDialog(
        settings = theme,
        onDismiss = onDismiss,
        onChangeDynamicColorPreference = viewModel::updateDynamicColorPreferences,
        onChangeDarkThemeConfig = viewModel::updateDarkThemeConfig,
        onOpenDebugTools = onOpenDebugTools
    )
}

@Composable
fun SettingsDialog(
    settings: SettingsUiState,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onDismiss: () -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    /** Opens the debug tools menu. Null, and the entry hidden, in release builds. */
    onOpenDebugTools: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text("Settings")
        },
        text = {
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                SettingsDialogSectionTitle(text = "Theme")
                AnimatedVisibility(visible = supportDynamicColor) {
                    Column {
                        SettingsDialogSectionTitle(text = "Use Dynamic Color")
                        Column(Modifier.selectableGroup()) {
                            SettingsDialogThemeChooserRow(
                                text = "Yes",
                                selected = settings.useDynamicColor,
                                onClick = { onChangeDynamicColorPreference(true) },
                            )
                            SettingsDialogThemeChooserRow(
                                text = "No",
                                selected = !settings.useDynamicColor,
                                onClick = { onChangeDynamicColorPreference(false) },
                            )
                        }
                    }
                }
                SettingsDialogSectionTitle(text = "Dark Mode Preferences")
                Column(Modifier.selectableGroup()) {
                    SettingsDialogThemeChooserRow(
                        text = "System default",
                        selected = settings.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
                        onClick = { onChangeDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM) },
                    )
                    SettingsDialogThemeChooserRow(
                        text = "Light",
                        selected = settings.darkThemeConfig == DarkThemeConfig.LIGHT,
                        onClick = { onChangeDarkThemeConfig(DarkThemeConfig.LIGHT) },
                    )
                    SettingsDialogThemeChooserRow(
                        text = "Dark",
                        selected = settings.darkThemeConfig == DarkThemeConfig.DARK,
                        onClick = { onChangeDarkThemeConfig(DarkThemeConfig.DARK) },
                    )
                }
                if (onOpenDebugTools != null) {
                    SettingsDialogSectionTitle(text = "Developer")
                    Text(
                        text = "Debug tools",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(role = Role.Button) { onOpenDebugTools() }
                            .padding(12.dp)
                    )
                }
            }
        },
        confirmButton = {
            Text(
                text = "OK",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() }
            )
        }
    )
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}
