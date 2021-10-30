package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.crew.Crew
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.statistics.graphs.padstats.LandingPad
import uk.co.zac_h.spacex.statistics.graphs.padstats.Launchpad
import uk.co.zac_h.spacex.types.DatePrecision
import uk.co.zac_h.spacex.utils.EventDate
import uk.co.zac_h.spacex.vehicles.cores.Core
import uk.co.zac_h.spacex.vehicles.rockets.Rocket
import uk.co.zac_h.spacex.vehicles.ships.Ship

data class Launch(
    val flightNumber: Int?,
    val missionName: String?,
    val launchDate: EventDate?,
    val datePrecision: DatePrecision?,
    val staticFireDate: EventDate?,
    val tbd: Boolean?,
    val net: Boolean?,
    val window: Int?,
    val rocketId: String? = null,
    val rocket: Rocket? = null,
    val success: Boolean?,
    val failures: List<LaunchFailures>?,
    val upcoming: Boolean?,
    val details: String?,
    val fairings: Fairings?,
    val crew: List<Crew>?,
    val shipIds: List<String>? = null,
    val ships: List<Ship>? = null,
    val capsules: List<String>?,
    val payloadIds: List<String>? = null,
    val payloads: List<Payload>? = null,
    val launchpadId: String? = null,
    val launchpad: Launchpad? = null,
    val cores: List<LaunchCore>?,
    val links: LaunchLinks?,
    val autoUpdate: Boolean?,
    val id: String
) {

    constructor(
        response: LaunchResponse
    ) : this(
        flightNumber = response.flightNumber,
        missionName = response.missionName,
        launchDate = EventDate(
            dateUtc = response.launchDateUtc,
            dateUnix = response.launchDateUnix,
            dateLocal = response.launchDateLocal
        ),
        datePrecision = response.datePrecision.toDatePrecision(),
        staticFireDate = EventDate(
            dateUtc = response.staticFireDateUtc,
            dateUnix = response.staticFireDateUnix
        ),
        tbd = response.tbd,
        net = response.net,
        window = response.window,
        rocketId = response.rocket,
        success = response.success,
        failures = response.failures,
        upcoming = response.upcoming,
        details = response.details,
        fairings = response.fairings,
        crew = response.crew?.map { Crew(it) },
        shipIds = response.ships,
        capsules = response.capsules,
        payloadIds = response.payloads,
        launchpadId = response.launchpad,
        cores = response.cores?.map { LaunchCore(it) },
        links = response.links,
        autoUpdate = response.autoUpdate,
        id = response.id
    )

    constructor(
        response: LaunchQueriedResponse
    ) : this(
        flightNumber = response.flightNumber,
        missionName = response.missionName,
        launchDate = EventDate(
            dateUtc = response.launchDateUtc,
            dateUnix = response.launchDateUnix,
            dateLocal = response.launchDateLocal
        ),
        datePrecision = response.datePrecision.toDatePrecision(),
        staticFireDate = EventDate(
            dateUtc = response.staticFireDateUtc,
            dateUnix = response.staticFireDateUnix
        ),
        tbd = response.tbd,
        net = response.net,
        window = response.window,
        rocketId = response.rocket?.id,
        rocket = response.rocket?.let { Rocket(it) },
        success = response.success,
        failures = response.failures,
        upcoming = response.upcoming,
        details = response.details,
        fairings = response.fairings,
        crew = response.crew?.map { Crew(it) },
        ships = response.ships?.map { Ship(it) },
        capsules = response.capsules,
        payloads = response.payloads?.map { Payload(it) },
        launchpad = response.launchpad?.let { Launchpad(it) },
        cores = response.cores?.map { LaunchCore(it) },
        links = response.links,
        autoUpdate = response.autoUpdate,
        id = response.id
    )

    companion object {
        private fun String?.toDatePrecision() = when (this) {
            SPACEX_LAUNCH_DATE_PRECISION_HALF -> DatePrecision.HALF
            SPACEX_LAUNCH_DATE_PRECISION_QUARTER -> DatePrecision.QUARTER
            SPACEX_LAUNCH_DATE_PRECISION_YEAR -> DatePrecision.YEAR
            SPACEX_LAUNCH_DATE_PRECISION_MONTH -> DatePrecision.MONTH
            SPACEX_LAUNCH_DATE_PRECISION_DAY -> DatePrecision.DAY
            SPACEX_LAUNCH_DATE_PRECISION_HOUR -> DatePrecision.HOUR
            else -> null
        }
    }
}

data class LaunchCore(
    var id: String?,
    var core: Core? = null,
    var flight: Int?,
    var gridfins: Boolean?,
    var legs: Boolean?,
    var reused: Boolean?,
    var landingAttempt: Boolean?,
    var landingSuccess: Boolean?,
    var landingType: String?,
    var landingPadId: String? = null,
    var landingPad: LandingPad? = null
) {

    constructor(
        response: LaunchCoreResponse
    ) : this(
        id = response.id,
        flight = response.flight,
        gridfins = response.gridfins,
        legs = response.legs,
        reused = response.reused,
        landingAttempt = response.landingAttempt,
        landingSuccess = response.landingSuccess,
        landingType = response.landingType,
        landingPadId = response.landingPad
    )

    constructor(
        response: LaunchCoreQueriedResponse
    ) : this(
        id = response.core?.id,
        core = response.core?.let { Core(it) },
        flight = response.flight,
        gridfins = response.gridfins,
        legs = response.legs,
        reused = response.reused,
        landingAttempt = response.landingAttempt,
        landingSuccess = response.landingSuccess,
        landingType = response.landingType,
        landingPad = response.landingPad?.let { LandingPad(it) }
    )

}

data class Payload(
    val name: String?,
    val type: String?,
    val reused: Boolean?,
    val launchId: String? = null,
    val launch: Launch? = null,
    val customers: List<String>?,
    val noradIds: List<Int>?,
    val nationalities: List<String>?,
    val manufacturers: List<String>?,
    val mass: Mass?,
    val formattedMass: MassFormatted?,
    val orbit: String?,
    val referenceSystem: String?,
    val regime: String?,
    val longitude: Float?,
    val semiMajorAxisKm: Float?,
    val eccentricity: Float?,
    val periapsisKm: Float?,
    val apoapsisKm: Float?,
    val inclination: Float?,
    val period: Float?,
    val lifespan: Int?,
    val epoch: String?,
    val meanMotion: Float?,
    val raan: Float?,
    val argOfPericenter: Float?,
    val meanAnomaly: Float?,
    val dragon: PayloadDragon?,
    val id: String
) {

    constructor(
        response: PayloadResponse
    ) : this(
        name = response.name,
        type = response.type,
        reused = response.reused,
        launchId = response.launch,
        customers = response.customers,
        noradIds = response.noradIds,
        nationalities = response.nationalities,
        manufacturers = response.manufacturers,
        mass = Mass(kg = response.massKg, lb = response.massLbs),
        formattedMass = MassFormatted.formatMass(response.massKg, response.massLbs),
        orbit = response.orbit,
        referenceSystem = response.referenceSystem,
        regime = response.regime,
        longitude = response.longitude,
        semiMajorAxisKm = response.semiMajorAxisKm,
        eccentricity = response.eccentricity,
        periapsisKm = response.periapsisKm,
        apoapsisKm = response.apoapsisKm,
        inclination = response.inclination,
        period = response.period,
        lifespan = response.lifespan,
        epoch = response.epoch,
        meanMotion = response.meanMotion,
        raan = response.raan,
        argOfPericenter = response.argOfPericenter,
        meanAnomaly = response.meanAnomaly,
        dragon = response.dragon?.let { PayloadDragon(it) },
        id = response.id
    )

    constructor(
        response: PayloadQueriedResponse
    ) : this(
        name = response.name,
        type = response.type,
        reused = response.reused,
        launch = response.launch?.let { Launch(it) },
        customers = response.customers,
        noradIds = response.noradIds,
        nationalities = response.nationalities,
        manufacturers = response.manufacturers,
        mass = Mass(kg = response.massKg, lb = response.massLbs),
        formattedMass = MassFormatted.formatMass(response.massKg, response.massLbs),
        orbit = response.orbit,
        referenceSystem = response.referenceSystem,
        regime = response.regime,
        longitude = response.longitude,
        semiMajorAxisKm = response.semiMajorAxisKm,
        eccentricity = response.eccentricity,
        periapsisKm = response.periapsisKm,
        apoapsisKm = response.apoapsisKm,
        inclination = response.inclination,
        period = response.period,
        lifespan = response.lifespan,
        epoch = response.epoch,
        meanMotion = response.meanMotion,
        raan = response.raan,
        argOfPericenter = response.argOfPericenter,
        meanAnomaly = response.meanAnomaly,
        dragon = response.dragon?.let { PayloadDragon(it) },
        id = response.id
    )
}

data class PayloadDragon(
    val capsule: String?,
    val massReturned: MassFormatted?,
    val flightTime: Int?,
    val manifest: String?,
    val waterLanding: Boolean?,
    val landLanding: Boolean?
) {

    constructor(
        response: PayloadDragonResponse
    ) : this(
        capsule = response.capsule,
        massReturned = MassFormatted.formatMass(response.massReturnedKg, response.massReturnedLbs),
        flightTime = response.flightTime,
        manifest = response.manifest,
        waterLanding = response.waterLanding,
        landLanding = response.landLanding
    )

}