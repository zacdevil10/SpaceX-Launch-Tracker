package uk.co.zac_h.spacex.feature.starlink.list

import android.text.format.DateUtils
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uk.co.zac_h.spacex.feature.starlink.R
import uk.co.zac_h.spacex.feature.starlink.SatelliteDetails
import uk.co.zac_h.spacex.feature.starlink.SatelliteListState
import uk.co.zac_h.spacex.feature.starlink.SatelliteStats
import uk.co.zac_h.spacex.feature.starlink.Stat

/**
 * Every satellite, searchable by name or NORAD id and filterable by shell and by the country it
 * is over right now. Tapping a row expands it with its details and ground track.
 */
@Composable
fun SatelliteListScreen(
    state: SatelliteListState,
    filter: SatelliteFilter,
    expandedId: Int?,
    details: SatelliteDetails?,
    onQuery: (String) -> Unit,
    onToggleShell: (Shell) -> Unit,
    onCountry: (String?) -> Unit,
    onClearFilters: () -> Unit,
    onToggleExpanded: (Int) -> Unit,
    onShowOnGlobe: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    var showCountryPicker by rememberSaveable { mutableStateOf(false) }
    val selectedCountry = filter.countryCode?.let { code -> state.countries.firstOrNull { it.code == code } }
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(key = "filters", contentType = "filters") {
            Filters(
                filter = filter,
                countryName = selectedCountry?.name,
                shown = state.rows.size,
                total = state.total,
                isLoading = state.isLoading,
                onQuery = onQuery,
                onToggleShell = onToggleShell,
                onPickCountry = { showCountryPicker = true },
                onClearFilters = onClearFilters
            )
        }

        when {
            state.isLoading -> item(key = "loading", contentType = "message") {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator()
                    Text(stringResource(R.string.starlink_list_loading), textAlign = TextAlign.Center)
                }
            }

            state.rows.isEmpty() -> item(key = "empty", contentType = "message") {
                Text(
                    text = stringResource(R.string.starlink_list_empty),
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            else -> items(state.rows, key = { it.noradId }, contentType = { "satellite" }) { row ->
                val expanded = row.noradId == expandedId
                SatelliteListItem(
                    row = row,
                    expanded = expanded,
                    details = details?.takeIf { expanded && it.noradId == row.noradId },
                    onClick = { onToggleExpanded(row.noradId) },
                    onShowOnGlobe = { onShowOnGlobe(row.noradId) }
                )
            }
        }
    }

    if (showCountryPicker) {
        CountryPicker(
            countries = state.countries,
            selected = filter.countryCode,
            onPick = {
                onCountry(it)
                showCountryPicker = false
            },
            onDismiss = { showCountryPicker = false }
        )
    }
}

@Composable
private fun Filters(
    filter: SatelliteFilter,
    countryName: String?,
    shown: Int,
    total: Int,
    isLoading: Boolean,
    onQuery: (String) -> Unit,
    onToggleShell: (Shell) -> Unit,
    onPickCountry: () -> Unit,
    onClearFilters: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedTextField(
            value = filter.query,
            onValueChange = onQuery,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(stringResource(R.string.starlink_search)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (filter.query.isNotEmpty()) {
                    IconButton(onClick = { onQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.starlink_clear_search))
                    }
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = filter.countryCode != null,
                onClick = onPickCountry,
                label = {
                    Text(
                        countryName?.let { stringResource(R.string.starlink_filter_country_selected, it) }
                            ?: stringResource(R.string.starlink_filter_country)
                    )
                },
                leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            )
            Shell.entries.forEach { shell ->
                FilterChip(
                    selected = shell in filter.shells,
                    onClick = { onToggleShell(shell) },
                    label = { Text(shell.label) }
                )
            }
        }
        if (!isLoading) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.starlink_list_count, "%,d".format(shown), "%,d".format(total)),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (filter.isActive) {
                    TextButton(onClick = onClearFilters) { Text(stringResource(R.string.starlink_filter_clear)) }
                }
            }
        }
    }
}

@Composable
private fun SatelliteListItem(
    row: SatelliteRow,
    expanded: Boolean,
    details: SatelliteDetails?,
    onClick: () -> Unit,
    onShowOnGlobe: () -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .clickable(onClick = onClick)
                .animateContentSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = row.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = listOf(
                            stringResource(R.string.starlink_norad_id, row.noradId),
                            row.shell.label,
                            row.fix?.let { stringResource(R.string.starlink_altitude_value, it.position.altitudeKm) }
                        ).filterNotNull().joinToString(" · "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = when {
                            row.fix == null -> stringResource(R.string.starlink_no_position)
                            row.countryName != null -> stringResource(R.string.starlink_over_country, row.countryName)
                            else -> stringResource(R.string.starlink_over_sea)
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
            if (expanded) {
                ExpandedDetails(details = details, onShowOnGlobe = onShowOnGlobe)
            }
        }
    }
}

@Composable
private fun ExpandedDetails(details: SatelliteDetails?, onShowOnGlobe: () -> Unit) {
    Column(
        modifier = Modifier.padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (details == null) {
            Box(modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }
        details.fix?.let { SatelliteStats(it) }
        Row(modifier = Modifier.fillMaxWidth().padding(end = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Stat(
                label = stringResource(R.string.starlink_inclination),
                value = stringResource(R.string.starlink_inclination_value, details.inclination)
            )
            Stat(
                label = stringResource(R.string.starlink_period),
                value = stringResource(R.string.starlink_period_value, details.shape.periodMinutes)
            )
            Stat(
                label = stringResource(R.string.starlink_perigee_apogee),
                value = stringResource(R.string.starlink_perigee_apogee_value, details.lowestKm, details.highestKm)
            )
        }
        Stat(
            label = stringResource(R.string.starlink_elements_age),
            value = DateUtils.getRelativeTimeSpanString(
                details.elementsEpochMillis,
                details.timeMillis.coerceAtLeast(details.elementsEpochMillis),
                DateUtils.MINUTE_IN_MILLIS
            ).toString()
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            GroundTrackMap(track = details.groundTrack, modifier = Modifier.fillMaxWidth())
            Text(
                text = stringResource(R.string.starlink_ground_track),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Button(onClick = onShowOnGlobe, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.starlink_show_on_globe))
        }
    }
}

@Composable
private fun CountryPicker(
    countries: List<CountryCount>,
    selected: String?,
    onPick: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val shown = remember(countries, query) {
        val q = query.trim()
        if (q.isEmpty()) countries else countries.filter { it.name.contains(q, ignoreCase = true) }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.starlink_country_picker_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text(stringResource(R.string.starlink_country_search)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                )
                LazyColumn(modifier = Modifier.heightIn(max = 360.dp)) {
                    if (query.isBlank()) {
                        item(key = "anywhere") {
                            CountryRow(
                                name = stringResource(R.string.starlink_country_anywhere),
                                count = null,
                                selected = selected == null,
                                onClick = { onPick(null) }
                            )
                            HorizontalDivider()
                        }
                    }
                    items(shown, key = { it.code }) { country ->
                        CountryRow(
                            name = country.name,
                            count = country.satellites,
                            selected = country.code == selected,
                            onClick = { onPick(country.code) }
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.starlink_cancel)) } }
    )
}

@Composable
private fun CountryRow(name: String, count: Int?, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        if (count != null) {
            Text(
                text = stringResource(R.string.starlink_country_count, count),
                style = MaterialTheme.typography.bodySmall,
                color = if (count > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
