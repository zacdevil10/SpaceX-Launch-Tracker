package uk.co.zac_h.spacex.feature.launch.container.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import uk.co.zac_h.spacex.core.ui.ComponentPreviews
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.launch.R

@Composable
fun LaunchDetailsHold(
    modifier: Modifier = Modifier,
    description: String
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
                text = stringResource(id = R.string.hold_label),
                fontWeight = FontWeight.Bold
            )
            Text(text = description)
        }
    }
}

@ComponentPreviews
@Composable
fun LaunchDetailsHoldPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    SpaceXTheme {
        LaunchDetailsHold(
            description = text.split("\n\n").first()
        )
    }
}