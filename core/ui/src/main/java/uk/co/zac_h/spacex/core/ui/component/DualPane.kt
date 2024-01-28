package uk.co.zac_h.spacex.core.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TwoPane(
    modifier: Modifier = Modifier,
    first: @Composable () -> Unit,
    second: @Composable () -> Unit,
    isTablet: Boolean,
    isDetailOpen: Boolean,
    hasDetailContent: Boolean,
    closeDetailScreen: () -> Unit
) {
    if (isTablet) {
        Row(
            modifier = modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f)
            ) {
                first()
            }
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f)
            ) {
                second()
            }
        }
    } else {
        AnimatedContent(
            targetState = isDetailOpen,
            label = "",
            transitionSpec = {
                if (targetState) {
                    (slideInHorizontally { width -> width / 2 } + fadeIn())
                        .togetherWith(slideOutHorizontally { width -> -(width / 2) } + fadeOut())
                } else {
                    (slideInHorizontally { width -> -(width / 2) } + fadeIn())
                        .togetherWith(slideOutHorizontally { width -> width / 2 } + fadeOut())
                }.using(SizeTransform(clip = false)).apply {
                    targetContentZIndex = if (targetState) 1f else 0f
                }
            }
        ) {
            if (it && hasDetailContent) {
                BackHandler {
                    closeDetailScreen()
                }
                second()
            } else {
                first()
            }
        }
    }
}
