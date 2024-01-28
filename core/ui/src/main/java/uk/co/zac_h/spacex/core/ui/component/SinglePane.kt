package uk.co.zac_h.spacex.core.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable

@Composable
fun <T> SinglePane(
    isDetailOpen: Boolean,
    list: @Composable () -> Unit,
    detailContent: T?,
    detail: @Composable (T) -> Unit,
    closeDetailScreen: () -> Unit
) {
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
        if (it && detailContent != null) {
            BackHandler {
                closeDetailScreen()
            }
            detail(detailContent)
        } else {
            list()
        }
    }
}
