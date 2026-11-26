package uk.co.zac_h.spacex.feature.starlink

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.database.StarlinkSatelliteEntity
import uk.co.zac_h.spacex.feature.starlink.countries.CountryRepository
import uk.co.zac_h.spacex.feature.starlink.globe.GlobeCamera
import uk.co.zac_h.spacex.feature.starlink.globe.SatelliteFrame
import uk.co.zac_h.spacex.feature.starlink.list.CountryCount
import uk.co.zac_h.spacex.feature.starlink.list.SatelliteFilter
import uk.co.zac_h.spacex.feature.starlink.list.SatelliteRow
import uk.co.zac_h.spacex.feature.starlink.list.Shell
import uk.co.zac_h.spacex.feature.starlink.list.countryCounts
import uk.co.zac_h.spacex.feature.starlink.list.filtered
import uk.co.zac_h.spacex.feature.starlink.list.shell
import uk.co.zac_h.spacex.feature.starlink.orbit.Constellation
import uk.co.zac_h.spacex.feature.starlink.orbit.OrbitShape
import uk.co.zac_h.spacex.feature.starlink.orbit.OrbitalElements
import uk.co.zac_h.spacex.feature.starlink.orbit.Satellite
import uk.co.zac_h.spacex.feature.starlink.orbit.SatelliteFix
import uk.co.zac_h.spacex.feature.starlink.orbit.TrackPoint
import uk.co.zac_h.spacex.feature.starlink.orbit.shape
import uk.co.zac_h.spacex.feature.starlink.orbit.sunDirectionScene
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.store.Clock
import uk.co.zac_h.spacex.network.store.StarlinkStore
import javax.inject.Inject

@HiltViewModel
class StarlinkViewModel @Inject constructor(
    private val store: StarlinkStore,
    private val countries: CountryRepository,
    private val clock: Clock
) : ViewModel() {

    /** Kept here so the view survives rotation. */
    val camera = GlobeCamera()

    private val refreshState = MutableStateFlow<ApiResult<Unit>>(ApiResult.Pending)
    private val simulation = MutableStateFlow(SimulationClock.live(clock.now()))
    private val selectedId = MutableStateFlow<Int?>(null)

    private val _mode = MutableStateFlow(StarlinkMode.Globe)
    val mode: StateFlow<StarlinkMode> = _mode

    private val _filter = MutableStateFlow(SatelliteFilter())
    val filter: StateFlow<SatelliteFilter> = _filter

    private val _expandedId = MutableStateFlow<Int?>(null)
    val expandedId: StateFlow<Int?> = _expandedId

    private val constellation: StateFlow<Constellation?> = store.satellites
        .map { entities -> Constellation(entities.map { it.toSatellite() }) }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    /**
     * Satellite positions at the simulated time, recomputed every
     * [SimulationClock.updateIntervalMillis] while the screen is visible.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val frames: StateFlow<SatelliteFrame?> =
        combine(constellation.filterNotNull(), simulation, selectedId, ::Triple)
            .flatMapLatest { (constellation, simulation, selectedId) ->
                flow {
                    while (true) {
                        emit(constellation.frame(simulation.timeAt(clock.now()), selectedId))
                        delay(simulation.updateIntervalMillis)
                    }
                }
            }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    /**
     * Every satellite with where it is and the country under it, refreshed every few seconds
     * while the list is visible. Null until the first pass (and the country borders) are ready.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private val rows: StateFlow<List<SatelliteRow>?> =
        combine(constellation.filterNotNull(), simulation, ::Pair)
            .flatMapLatest { (constellation, simulation) ->
                flow {
                    val lookup = countries.lookup()
                    val shells = constellation.satellites.map { it.shell() }
                    while (true) {
                        val fixes = constellation.fixes(simulation.timeAt(clock.now()))
                        emit(constellation.satellites.mapIndexed { index, satellite ->
                            val fix = fixes[index]
                            val country = fix?.let { lookup.countryAt(it.position.latitude, it.position.longitude) }
                            SatelliteRow(satellite.noradId, satellite.name, shells[index], fix, country?.code, country?.name)
                        })
                        delay(if (simulation.speed == 1) LIST_INTERVAL_MILLIS else LIST_INTERVAL_FAST_MILLIS)
                    }
                }
            }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val allCountries: StateFlow<List<Pair<String, String>>> = flow {
        emit(countries.lookup().countries.map { it.code to it.name }.distinctBy { it.first })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val listState: StateFlow<SatelliteListState> = combine(rows, _filter, allCountries) { rows, filter, countries ->
        if (rows == null) {
            SatelliteListState(isLoading = true)
        } else {
            SatelliteListState(
                isLoading = false,
                rows = rows.filtered(filter),
                total = rows.size,
                countries = rows.countryCounts(countries)
            )
        }
    }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SatelliteListState())

    /** The expanded row's details and ground track, refreshed while it stays open. */
    @OptIn(ExperimentalCoroutinesApi::class)
    val expandedDetails: StateFlow<SatelliteDetails?> =
        combine(constellation.filterNotNull(), _expandedId, simulation, ::Triple)
            .flatMapLatest { (constellation, expandedId, simulation) ->
                val index = expandedId?.let(constellation::indexOf) ?: -1
                if (index < 0) flowOf(null) else flow<SatelliteDetails?> {
                    val satellite = constellation.satellites[index]
                    val shape = satellite.elements.shape()
                    while (true) {
                        val now = simulation.timeAt(clock.now())
                        val track = constellation.groundTrack(
                            index,
                            now,
                            minutesBefore = shape.periodMinutes / 2,
                            minutesAfter = shape.periodMinutes * 1.5
                        )
                        // Over the next full orbit, on the same oblate Earth as the altitude shown,
                        // so the current altitude always sits between the two.
                        val nextOrbit = track.filter { it.minutesFromNow in 0.0..shape.periodMinutes }
                        emit(
                            SatelliteDetails(
                                noradId = satellite.noradId,
                                fix = constellation.fix(index, now),
                                shape = shape,
                                inclination = satellite.elements.inclination,
                                elementsEpochMillis = satellite.elements.epochMillis,
                                timeMillis = now,
                                lowestKm = nextOrbit.minOfOrNull { it.altitudeKm } ?: shape.perigeeKm,
                                highestKm = nextOrbit.maxOfOrNull { it.altitudeKm } ?: shape.apogeeKm,
                                groundTrack = track
                            )
                        )
                        delay(if (simulation.speed == 1) DETAILS_INTERVAL_MILLIS else LIST_INTERVAL_FAST_MILLIS)
                    }
                }
            }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val uiState: StateFlow<StarlinkUiState> = combine(
        constellation.map { it?.size ?: 0 },
        store.fetchedAt,
        refreshState,
        simulation
    ) { count, fetchedAt, refresh, simulation ->
        StarlinkUiState(
            satelliteCount = count,
            fetchedAt = fetchedAt,
            refresh = refresh,
            speed = simulation.speed,
            isLive = simulation.isLive(clock.now())
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StarlinkUiState())

    init {
        refresh()
    }

    fun refresh(force: Boolean = false) {
        viewModelScope.launch {
            refreshState.value = ApiResult.Pending
            val result = store.refresh(force)
            if (result is ApiResult.Failure) Log.w(TAG, "Couldn't refresh Starlink orbits", result.exception)
            refreshState.value = result
        }
    }

    fun setSpeed(speed: Int) {
        simulation.update { it.withSpeed(speed, clock.now()) }
    }

    /** Back to real time at normal speed. */
    fun goLive() {
        simulation.value = SimulationClock.live(clock.now())
    }

    /** Selects the satellite drawn nearest the tap, or clears the selection if there is none. */
    fun onTap(position: Offset, width: Int, height: Int, radiusPx: Float) {
        val frame = frames.value ?: return
        val index = camera.pick(position.x, position.y, width, height, frame.positions, radiusPx)
        selectedId.value = constellation.value?.satellites?.getOrNull(index)?.noradId
    }

    fun clearSelection() {
        selectedId.value = null
    }

    fun setMode(mode: StarlinkMode) {
        _mode.value = mode
    }

    fun setQuery(query: String) {
        _filter.update { it.copy(query = query) }
    }

    fun toggleShell(shell: Shell) {
        _filter.update { it.copy(shells = if (shell in it.shells) it.shells - shell else it.shells + shell) }
    }

    fun setCountry(code: String?) {
        _filter.update { it.copy(countryCode = code) }
    }

    fun clearFilters() {
        _filter.value = SatelliteFilter()
    }

    /** Expands the row for [noradId], or collapses it if it's already open. */
    fun toggleExpanded(noradId: Int) {
        _expandedId.update { if (it == noradId) null else noradId }
    }

    /** Switches to the globe with [noradId] selected and turned to face the camera. */
    fun showOnGlobe(noradId: Int) {
        selectedId.value = noradId
        val constellation = constellation.value
        val index = constellation?.indexOf(noradId) ?: -1
        if (constellation != null && index >= 0) {
            constellation.fix(index, simulation.value.timeAt(clock.now()))?.let {
                camera.lookAt(it.position.latitude, it.position.longitude)
            }
        }
        _mode.value = StarlinkMode.Globe
    }

    private fun Constellation.frame(timeMillis: Long, selectedId: Int?): SatelliteFrame {
        val selected = selectedId?.let(::indexOf) ?: -1
        return SatelliteFrame(
            timeMillis = timeMillis,
            positions = scenePositions(timeMillis),
            sun = sunDirectionScene(timeMillis),
            selected = selected,
            orbit = if (selected >= 0) orbitPath(selected, timeMillis) else null,
            details = if (selected >= 0) fix(selected, timeMillis) else null
        )
    }
}

private const val TAG = "StarlinkViewModel"
private const val LIST_INTERVAL_MILLIS = 3_000L
private const val LIST_INTERVAL_FAST_MILLIS = 500L
private const val DETAILS_INTERVAL_MILLIS = 1_000L

enum class StarlinkMode { Globe, List }

data class SatelliteListState(
    val isLoading: Boolean = true,
    /** The rows matching the filter. */
    val rows: List<SatelliteRow> = emptyList(),
    /** All satellites, before filtering. */
    val total: Int = 0,
    val countries: List<CountryCount> = emptyList()
)

data class SatelliteDetails(
    val noradId: Int,
    val fix: SatelliteFix?,
    val shape: OrbitShape,
    val inclination: Double,
    val elementsEpochMillis: Long,
    val timeMillis: Long,
    /** Lowest and highest altitude over the next orbit, in km. */
    val lowestKm: Double,
    val highestKm: Double,
    val groundTrack: List<TrackPoint>
)

data class StarlinkUiState(
    val satelliteCount: Int = 0,
    val fetchedAt: Long? = null,
    val refresh: ApiResult<Unit> = ApiResult.Pending,
    val speed: Int = 1,
    val isLive: Boolean = true
)

internal fun StarlinkSatelliteEntity.toSatellite() = Satellite(
    noradId = noradId,
    name = name,
    elements = OrbitalElements(
        epochMillis = epoch,
        meanMotion = meanMotion,
        eccentricity = eccentricity,
        inclination = inclination,
        raan = raOfAscNode,
        argOfPerigee = argOfPericenter,
        meanAnomaly = meanAnomaly,
        bstar = bstar
    )
)
