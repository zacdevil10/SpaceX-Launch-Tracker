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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.database.StarlinkSatelliteEntity
import uk.co.zac_h.spacex.feature.starlink.globe.GlobeCamera
import uk.co.zac_h.spacex.feature.starlink.globe.SatelliteFrame
import uk.co.zac_h.spacex.feature.starlink.orbit.Constellation
import uk.co.zac_h.spacex.feature.starlink.orbit.OrbitalElements
import uk.co.zac_h.spacex.feature.starlink.orbit.Satellite
import uk.co.zac_h.spacex.feature.starlink.orbit.sunDirectionScene
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.store.Clock
import uk.co.zac_h.spacex.network.store.StarlinkStore
import javax.inject.Inject

@HiltViewModel
class StarlinkViewModel @Inject constructor(
    private val store: StarlinkStore,
    private val clock: Clock
) : ViewModel() {

    /** Kept here so the view survives rotation. */
    val camera = GlobeCamera()

    private val refreshState = MutableStateFlow<ApiResult<Unit>>(ApiResult.Pending)
    private val simulation = MutableStateFlow(SimulationClock.live(clock.now()))
    private val selectedId = MutableStateFlow<Int?>(null)

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
