package uk.co.zac_h.spacex.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LabelValue(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .padding(start = 16.dp),
            text = value,
            textAlign = TextAlign.End
        )
    }
}

@Composable
@Preview
fun LabelValuePreview() {
    LabelValue(
        modifier = Modifier
            .background(Color.White),
        label = "Label",
        value = "Value"
    )
}

@Composable
@Preview
fun LabelValueMultilinePreview() {
    LabelValue(
        modifier = Modifier
            .background(Color.White),
        label = "Label",
        value = "Value\nValue 2"
    )
}