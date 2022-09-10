package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.dto.spacex.MassFormatted.Companion.formatMass
import uk.co.zac_h.spacex.utils.EventDate
import java.text.DecimalFormat

data class RoadsterResponse(
    @field:Json(name = SPACEX_FIELD_ROADSTER_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_LAUNCH_DATE_UTC) val launchDateUtc: String?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_LAUNCH_DATE_UNIX) val launchDateUnix: Long?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_LAUNCH_MASS_KG) val launchMassKg: Float?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_LAUNCH_MASS_LBS) val launchMassLbs: Float?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_NORAD_ID) val noradId: Int?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_EPOCH_JD) val epochJd: Int?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_ORBIT_TYPE) val orbitType: String?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_APOAPSIS_AU) val apoapsisAu: Int?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_PERIAPSIS_AU) val periapsisAu: Int?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_SEMI_MAJOR_AXIS_AU) val semiMajorAxisAu: Int?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_ECCENTRICITY) val eccentricity: Int?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_INCLINATION) val inclination: Int?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_LNG) val longitude: Float?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_PERIAPSIS_ARG) val periapsisArg: Int?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_PERIOD_DAYS) val periodDays: Int?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_SPEED_KPH) val speedKph: Int?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_SPEED_MPH) val speedMph: Int?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_EARTH_DISTANCE_KM) val earthDistanceKm: Double?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_EARTH_DISTANCE_MI) val earthDistanceMi: Double?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_MARS_DISTANCE_KM) val marsDistanceKm: Double?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_MARS_DISTANCE_MI) val marsDistanceMi: Double?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_FLICKR_IMAGES) val flickr: List<String>?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_WIKI) val wikipedia: String?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_VIDEO) val video: String?,
    @field:Json(name = SPACEX_FIELD_ROADSTER_DETAILS) val details: String?
)

data class Roadster(
    val name: String?,
    val launchDate: EventDate?,
    val launchMass: MassFormatted?,
    val noradId: Int?,
    val epochJd: Int?,
    val orbitType: String?,
    val apoapsisAu: Int?,
    val periapsisAu: Int?,
    val semiMajorAxisAu: Int?,
    val eccentricity: Int?,
    val inclination: Int?,
    val longitude: Float?,
    val periapsisArg: Int?,
    val periodDays: Int?,
    val velocity: Velocity?,
    val earthDistance: Distance?,
    val marsDistance: Distance?,
    val flickr: List<String>?,
    val wikipedia: String?,
    val video: String?,
    val details: String?
) {

    constructor(
        response: RoadsterResponse
    ) : this(
        name = response.name,
        launchDate = EventDate(
            dateUtc = response.launchDateUtc,
            dateUnix = response.launchDateUnix
        ),
        launchMass = formatMass(response.launchMassKg, response.launchMassLbs),
        noradId = response.noradId,
        epochJd = response.epochJd,
        orbitType = response.orbitType,
        apoapsisAu = response.apoapsisAu,
        periapsisAu = response.periapsisAu,
        semiMajorAxisAu = response.semiMajorAxisAu,
        eccentricity = response.eccentricity,
        inclination = response.inclination,
        longitude = response.longitude,
        periapsisArg = response.periapsisArg,
        periodDays = response.periodDays,
        velocity = Velocity(kph = response.speedKph, mph = response.speedMph),
        earthDistance = Distance(
            km = response.earthDistanceKm.format(),
            mi = response.earthDistanceMi.format()
        ),
        marsDistance = Distance(
            km = response.marsDistanceKm.format(),
            mi = response.marsDistanceMi.format()
        ),
        flickr = response.flickr,
        wikipedia = response.wikipedia,
        video = response.video,
        details = response.details
    )

    companion object {
        private fun Double?.format() = DecimalFormat("#,###.00").format(this)
    }

}
