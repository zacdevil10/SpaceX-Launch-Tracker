package uk.co.zac_h.spacex.core.model

import java.time.Instant
import java.time.LocalDate

/**
 * A rocket configuration, such as Falcon 9 Block 5. Units as Launch Library sends them: length and
 * diameter in metres, launch mass in tonnes, LEO and GTO capacity in kg, takeoff thrust in kN.
 */
data class Rocket(
    val id: Int,
    val name: String?,
    val fullName: String?,
    val type: RocketType?,
    val family: RocketFamily,
    val description: String?,
    val imageUrl: String?,
    val maxStages: Int?,
    val length: Double?,
    val diameter: Double?,
    val maidenFlight: LocalDate?,
    val launchMass: Double?,
    val leoCapacity: Double?,
    val gtoCapacity: Double?,
    val takeoffThrust: Double?,
    val consecutiveSuccessfulLaunches: Int?,
    val successfulLaunches: Int?,
    val failedLaunches: Int?,
    val pendingLaunches: Int?,
)

/** One physical first stage booster, such as B1062. */
data class Launcher(
    val id: Int,
    val serial: String?,
    val status: String?,
    val details: String?,
    /** The rocket configuration's full name, such as "Falcon 9 Block 5". */
    val configuration: String?,
    val imageUrl: String?,
    val flights: Int?,
    val firstLaunch: Instant?,
    val lastLaunch: Instant?,
)

/** One physical spacecraft, such as Dragon C206 "Endeavour". */
data class Spacecraft(
    val id: Int,
    val name: String?,
    val serial: String?,
    val status: String?,
    val description: String?,
    /** The configuration's name, such as "Crew Dragon". */
    val configuration: String?,
    val imageUrl: String?,
)

/** A spacecraft design, such as Crew Dragon or Cargo Dragon 2. Sizes in metres, payload in kg. */
data class SpacecraftConfiguration(
    val id: Int,
    val name: String?,
    val inUse: Boolean?,
    val capability: String?,
    val history: String?,
    val details: String?,
    val maidenFlight: LocalDate?,
    val height: Double?,
    val diameter: Double?,
    val humanRated: Boolean?,
    val crewCapacity: Int?,
    val payloadCapacity: Int?,
    val flightLife: String?,
    val imageUrl: String?,
    val wikiUrl: String?,
    val infoUrl: String?,
)
