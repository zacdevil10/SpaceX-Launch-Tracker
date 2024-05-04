package uk.co.zac_h.spacex.feature.settings.company

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.core.common.NetworkContent
import uk.co.zac_h.spacex.core.ui.DevicePreviews
import uk.co.zac_h.spacex.core.ui.LabelValue
import uk.co.zac_h.spacex.core.ui.SpaceXTheme
import uk.co.zac_h.spacex.feature.settings.R
import uk.co.zac_h.spacex.network.ApiResult

@Composable
fun CompanyScreen(
    contentType: ContentType,
    viewModel: CompanyViewModel = hiltViewModel()
) {
    val company by viewModel.company.collectAsStateWithLifecycle(ApiResult.Pending)

    CompanyContent(
        contentType = contentType,
        result = company,
        retry = viewModel::getCompany
    )
}

@Composable
fun CompanyContent(
    modifier: Modifier = Modifier,
    contentType: ContentType,
    result: ApiResult<Company>,
    retry: () -> Unit
) {
    when (contentType) {
        ContentType.SINGLE_PANE -> CompanySinglePane(
            modifier = modifier,
            result = result,
            retry = retry
        )

        ContentType.DUAL_PANE -> CompanyDualPane(
            modifier = modifier,
            result = result,
            retry = retry
        )
    }
}

@Composable
fun CompanySinglePane(
    modifier: Modifier = Modifier,
    result: ApiResult<Company>,
    retry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CompanyDetails(
            modifier = Modifier.fillMaxSize(),
            result = result,
            retry = retry
        )
    }
}

@Composable
fun CompanyDualPane(
    modifier: Modifier = Modifier,
    result: ApiResult<Company>,
    retry: () -> Unit
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
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    Modifier
                        .width(400.dp)
                ) {
                    CompanyDetails(
                        result = result,
                        retry = retry
                    )
                }
            }
        }
    }
}

@Composable
fun CompanyDetails(
    modifier: Modifier = Modifier,
    result: ApiResult<Company>,
    retry: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    NetworkContent(
        modifier = modifier,
        result = result,
        retry = retry
    ) { company ->
        Spacer(modifier = Modifier.height(64.dp))
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.ic_spacex_logo),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )
        Spacer(modifier = Modifier.height(48.dp))
        company.description?.let { Text(text = it) }
        company.website?.let {
            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                onClick = { uriHandler.openUri(it) }
            ) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_web_24),
                    contentDescription = ""
                )
                Text(text = "Website")
            }
        }
        company.wiki?.let {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                onClick = { uriHandler.openUri(it) }
            ) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.ic_wikipedia),
                    contentDescription = ""
                )
                Text(text = "Wikipedia")
            }
        }
        company.foundingYear?.let {
            LabelValue(
                modifier = Modifier
                    .padding(top = 16.dp),
                label = stringResource(id = R.string.founded_label),
                value = it
            )
        }
        company.administrator?.let {
            LabelValue(
                modifier = Modifier
                    .padding(top = 16.dp),
                label = stringResource(id = R.string.ceo_label),
                value = it.removePrefix("CEO: ")
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@DevicePreviews
@Composable
fun CompanyContentPreview() {
    SpaceXTheme {
        CompanyContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            contentType = ContentType.SINGLE_PANE,
            result = ApiResult.Success(
                Company(
                    id = 0,
                    name = "SpaceX",
                    description = "Description",
                    administrator = "Admin",
                    foundingYear = "2002",
                    totalLaunchCount = 800,
                    website = "url",
                    wiki = "wiki_url"
                )
            ),
            retry = {}
        )
    }
}