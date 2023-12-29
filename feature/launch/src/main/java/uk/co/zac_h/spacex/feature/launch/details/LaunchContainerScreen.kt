package uk.co.zac_h.spacex.feature.launch.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.co.zac_h.spacex.core.ui.PagerItem
import uk.co.zac_h.spacex.core.ui.SpaceXAppBar
import uk.co.zac_h.spacex.core.ui.SpaceXTabLayout
import uk.co.zac_h.spacex.feature.launch.LaunchesViewModel
import uk.co.zac_h.spacex.feature.launch.details.cores.LaunchCoresScreen
import uk.co.zac_h.spacex.feature.launch.details.crew.LaunchCrewScreen
import uk.co.zac_h.spacex.feature.launch.details.details.LaunchDetailsScreen

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LaunchContainerScreen(
    modifier: Modifier = Modifier,
    viewModel: LaunchesViewModel = viewModel(),
    navigateUp: () -> Unit
) {
    val screens = listOfNotNull(
        PagerItem("Details") {
            LaunchDetailsScreen(launch = viewModel.launch)
        },
        PagerItem("First stage") {
            LaunchCoresScreen(cores = viewModel.cores)
        },
        if (!viewModel.launch.crew.isNullOrEmpty()) {
            PagerItem("Crew") {
                viewModel.launch.crew?.let { LaunchCrewScreen(crew = it) }
            }
        } else null
    )

    val pagerState = rememberPagerState(pageCount = {
        screens.size
    })

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                SpaceXAppBar(
                    label = viewModel.launch.missionName,
                    navigationIcon = Icons.Filled.ArrowBack,
                    navigationIconAction = { navigateUp() },
                    scrollBehavior = scrollBehavior
                )
                SpaceXTabLayout(pagerState = pagerState, screens = screens)
            }
        }
    ) {
        HorizontalPager(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight(),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { page ->
            screens[page].screen()
        }
    }
}
