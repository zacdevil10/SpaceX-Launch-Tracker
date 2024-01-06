package uk.co.zac_h.spacex.feature.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text("Settings")
        },
        text = {
            Divider()
            Column {
                SettingsDialogSectionTitle(text = "Dark mode preferences")
                Column(Modifier.selectableGroup()) {
                    SettingsDialogThemeChooserRow(
                        text = "System default",
                        selected = viewModel.repository.themeMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
                        onClick = {
                            viewModel.repository.isTheme =
                                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        },
                    )
                    SettingsDialogThemeChooserRow(
                        text = "Light",
                        selected = viewModel.repository.themeMode == AppCompatDelegate.MODE_NIGHT_NO,
                        onClick = {
                            viewModel.repository.isTheme = AppCompatDelegate.MODE_NIGHT_NO
                        },
                    )
                    SettingsDialogThemeChooserRow(
                        text = "Dark",
                        selected = viewModel.repository.themeMode == AppCompatDelegate.MODE_NIGHT_YES,
                        onClick = {
                            viewModel.repository.isTheme = AppCompatDelegate.MODE_NIGHT_YES
                        },
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
