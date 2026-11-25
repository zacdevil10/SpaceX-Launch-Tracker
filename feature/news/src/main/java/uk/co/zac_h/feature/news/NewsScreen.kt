package uk.co.zac_h.feature.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.co.zac_h.feature.news.articles.ArticlesScreen
import uk.co.zac_h.spacex.core.common.ContentType

@Composable
fun NewsScreen(
    contentType: ContentType
) {
    when (contentType) {
        ContentType.SINGLE_PANE -> ArticlesScreen(contentType = contentType)
        ContentType.DUAL_PANE -> NewsTwoPaneContent(contentType = contentType)
    }
}

@Composable
fun NewsTwoPaneContent(
    modifier: Modifier = Modifier,
    contentType: ContentType
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            ArticlesScreen(contentType = contentType)
        }
    }
}
