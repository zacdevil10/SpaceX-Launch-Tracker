package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.formatMass

data class PayloadDocsModel(
    @field:Json(name = "docs") val docs: List<PayloadQueriedResponse>
)

data class PayloadResponse(
    @field:Json(name = SPACEX_FIELD_PAYLOAD_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_TYPE) val type: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_REUSED) val reused: Boolean?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_LAUNCH) val launch: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_CUSTOMERS) val customers: List<String>?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_NORAD_IDS) val noradIds: List<Int>?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_NATIONALITIES) val nationalities: List<String>?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_MANUFACTURERS) val manufacturers: List<String>?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_MASS_KG) val massKg: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_MASS_LBS) val massLbs: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_ORBIT) val orbit: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_REFERENCE_SYSTEM) val referenceSystem: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_REGIME) val regime: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_LONGITUDE) val longitude: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_SEMI_MAJOR_AXIS_KM) val semiMajorAxisKm: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_ECCENTRICITY) val eccentricity: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_PERIAPSIS_KM) val periapsisKm: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_APOAPSIS_KM) val apoapsisKm: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_INCLINATION_DEG) val inclination: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_PERIOD_MIN) val period: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_LIFESPAN_YEARS) val lifespan: Int?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_EPOCH) val epoch: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_MEAN_MOTION) val meanMotion: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_RAAN) val raan: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_ARG_OF_PERICENTER) val argOfPericenter: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_MEAN_ANOMALY) val meanAnomaly: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_DRAGON) val dragon: PayloadDragonResponse?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class PayloadQueriedResponse(
    @field:Json(name = SPACEX_FIELD_PAYLOAD_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_TYPE) val type: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_REUSED) val reused: Boolean?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_LAUNCH) val launch: LaunchResponse?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_CUSTOMERS) val customers: List<String>?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_NORAD_IDS) val noradIds: List<Int>?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_NATIONALITIES) val nationalities: List<String>?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_MANUFACTURERS) val manufacturers: List<String>?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_MASS_KG) val massKg: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_MASS_LBS) val massLbs: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_ORBIT) val orbit: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_REFERENCE_SYSTEM) val referenceSystem: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_REGIME) val regime: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_LONGITUDE) val longitude: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_SEMI_MAJOR_AXIS_KM) val semiMajorAxisKm: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_ECCENTRICITY) val eccentricity: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_PERIAPSIS_KM) val periapsisKm: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_APOAPSIS_KM) val apoapsisKm: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_INCLINATION_DEG) val inclination: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_PERIOD_MIN) val period: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_LIFESPAN_YEARS) val lifespan: Int?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_EPOCH) val epoch: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_MEAN_MOTION) val meanMotion: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_RAAN) val raan: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_ARG_OF_PERICENTER) val argOfPericenter: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_MEAN_ANOMALY) val meanAnomaly: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_DRAGON) val dragon: PayloadDragonResponse?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class PayloadDragonResponse(
    @field:Json(name = SPACEX_FIELD_PAYLOAD_DRAGON_CAPSULE) val capsule: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_DRAGON_MASS_RETURNED_KG) val massReturnedKg: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_DRAGON_MASS_RETURNED_LBS) val massReturnedLbs: Float?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_DRAGON_FLIGHT_TIME_SEC) val flightTime: Int?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_DRAGON_MANIFEST) val manifest: String?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_DRAGON_WATER_LANDING) val waterLanding: Boolean?,
    @field:Json(name = SPACEX_FIELD_PAYLOAD_DRAGON_LAND_LANDING) val landLanding: Boolean?
)

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
        formattedMass = formatMass(response.massKg, response.massLbs),
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
        formattedMass = formatMass(response.massKg, response.massLbs),
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
        massReturned = formatMass(response.massReturnedKg, response.massReturnedLbs),
        flightTime = response.flightTime,
        manifest = response.manifest,
        waterLanding = response.waterLanding,
        landLanding = response.landLanding
    )

}