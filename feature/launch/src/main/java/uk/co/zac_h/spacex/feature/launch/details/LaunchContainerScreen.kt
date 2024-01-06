package uk.co.zac_h.spacex.feature.launch.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import uk.co.zac_h.spacex.core.ui.PagerItem
import uk.co.zac_h.spacex.core.ui.SpaceXAppBar
import uk.co.zac_h.spacex.core.ui.SpaceXTabLayout
import uk.co.zac_h.spacex.feature.launch.LaunchItem
import uk.co.zac_h.spacex.feature.launch.details.cores.LaunchCoresScreen
import uk.co.zac_h.spacex.feature.launch.details.crew.LaunchCrewScreen
import uk.co.zac_h.spacex.feature.launch.details.details.LaunchDetailsScreen

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LaunchContainerScreen(
    modifier: Modifier = Modifier,
    launch: LaunchItem,
    isFullscreen: Boolean = true,
    navigateUp: () -> Unit = {}
) {
    val screens = listOfNotNull(
        PagerItem("Details") {
            LaunchDetailsScreen(launch = launch, isFullscreen = isFullscreen)
        },
        PagerItem("First stage") {
            LaunchCoresScreen(cores = launch.cores)
        },
        if (!launch.crew.isNullOrEmpty()) {
            PagerItem("Crew") {
                LaunchCrewScreen(crew = launch.crew)
            }
        } else null
    )

    val pagerState = rememberPagerState(pageCount = { screens.size })

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                if (isFullscreen) SpaceXAppBar(
                    label = launch.missionName,
                    navigationIcon = Icons.Filled.ArrowBack,
                    navigationIconAction = { navigateUp() }
                )
                SpaceXTabLayout(pagerState = pagerState, screens = screens)
            }
        }
    ) { padding ->
        HorizontalPager(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight(),
            state = pagerState,
            beyondBoundsPageCount = 1,
            verticalAlignment = Alignment.Top,
            key = { screens[it].label }
        ) { page ->
            screens[page].screen()
        }
    }
}
