package uk.co.zac_h.spacex.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter

@Composable
fun DynamicAsyncImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String?,
    placeholder: Painter,
    tint: Color = LocalContentColor.current,
    contentScale: ContentScale = ContentScale.Fit,
    placeholderScale: ContentScale = ContentScale.Fit
) {
    var isLoading by remember { mutableStateOf(true) }
    var isSuccess by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    val imageLoader = rememberAsyncImagePainter(
        model = model,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isSuccess = state is AsyncImagePainter.State.Success
            isError = state is AsyncImagePainter.State.Error
        },
    )

    val isLocalInspection = LocalInspectionMode.current

    Box(
        modifier = modifier,
    ) {
        if (isLoading && !isLocalInspection) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = if (isError.not() && !isLocalInspection) imageLoader else placeholder,
            contentDescription = contentDescription,
            colorFilter = if (isSuccess) null else ColorFilter.tint(tint),
            contentScale = if (isError.not() && !isLocalInspection) contentScale else placeholderScale
        )
    }
}

@Composable
fun DynamicAsyncImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String?,
    placeholder: ImageVector,
    tint: Color = LocalContentColor.current,
    contentScale: ContentScale = ContentScale.Fit
) = DynamicAsyncImage(
    modifier = modifier,
    model = model,
    contentDescription = contentDescription,
    placeholder = rememberVectorPainter(image = placeholder),
    tint = tint,
    contentScale = contentScale
)
