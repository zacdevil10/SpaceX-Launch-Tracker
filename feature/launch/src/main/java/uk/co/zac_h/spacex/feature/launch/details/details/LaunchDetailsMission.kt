package uk.co.zac_h.spacex.feature.launch.details.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.LabelValue
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.launch.R

@Composable
fun LaunchDetailsMission(
    modifier: Modifier = Modifier,
    description: String? = null,
    type: String? = null,
    orbit: String? = null
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(id = R.string.mission_label),
                fontWeight = FontWeight.Bold
            )
            description?.let {
                Text(modifier = Modifier.padding(bottom = 8.dp), text = it)
            }
            type?.let {
                LabelValue(
                    modifier = Modifier.padding(bottom = 8.dp),
                    label = "Type:",
                    value = it
                )
            }
            orbit?.let {
                LabelValue(label = "Orbit:", value = it)
            }
        }
    }
}

@ComponentPreviews
@Composable
fun LaunchDetailsMissionPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        LaunchDetailsMission(
            description = text.split("\n\n").first(),
            type = "Government",
            orbit = "Elliptical Orbit"
        )
    }
}