package uk.co.zac_h.spacex.feature.assets.vehicles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uk.co.zac_h.spacex.core.common.utils.TextResource
import uk.co.zac_h.spacex.core.ui.DevicePreviews
import uk.co.zac_h.spacex.core.ui.LabelValue
import uk.co.zac_h.spacex.core.ui.SpaceXAppBar
import uk.co.zac_h.spacex.core.ui.SpaceXTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailsScreen(
    modifier: Modifier = Modifier,
    vehicle: VehicleItem,
    isFullscreen: Boolean = true,
    navigateUp: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (isFullscreen) SpaceXAppBar(
                label = vehicle.title.orEmpty(),
                navigationIcon = Icons.Filled.ArrowBack,
                navigationIconAction = { navigateUp() }
            )
        }
    ) { contentPadding ->
        VehicleDetailsContent(
            modifier = Modifier.padding(contentPadding),
            vehicle = vehicle,
            isFullscreen = isFullscreen
        )
    }
}

@Composable
private fun VehicleDetailsContent(
    modifier: Modifier = Modifier,
    vehicle: VehicleItem,
    isFullscreen: Boolean
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            if (isFullscreen) vehicle.imageUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "",
                    modifier = Modifier
                        .height(256.dp)
                        .fillMaxWidth()
                        .clip(CardDefaults.elevatedShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            vehicle.longDescription?.let {
                Text(text = it)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        items(vehicle.specs) {
            LabelValue(label = it.label.asString(), value = it.value.asString())
        }
    }
}

@DevicePreviews
@Composable
fun VehicleDetailsContentPreview() {
    SpaceXTheme {
        VehicleDetailsContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            vehicle = object : VehicleItem {
                override val id: Any = "id"
                override val title: String = "Title"
                override val imageUrl: String? = null
                override val specs: List<SpecsItem> = listOf(
                    SpecsItem(
                        TextResource.string("Label"),
                        TextResource.string("Value")
                    )
                )
                override val longDescription: String = "This is a long description"
            },
            isFullscreen = true
        )
    }
}