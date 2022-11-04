package uk.co.zac_h.spacex.vehicles.ships

import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.network.dto.spacex.MassFormatted
import uk.co.zac_h.spacex.network.dto.spacex.ShipQueriedResponse

data class Ship(
    val name: String?,
    val legacyId: String?,
    val model: String?,
    val type: String?,
    val roles: List<String>?,
    val active: Boolean?,
    val imo: Int?,
    val mmsi: Int?,
    val abs: Int?,
    val shipClass: Int?,
    val mass: MassFormatted?,
    val yearBuilt: Int?,
    val homePort: String?,
    val status: String?,
    val speed: Int?,
    val course: Int?,
    val lat: Float?,
    val lng: Float?,
    val lastUpdate: String?,
    val link: String?,
    val image: String?,
    val launchIds: List<String>? = null,
    val launches: List<Launch>? = null,
    val id: String
) {

    constructor(
        response: ShipQueriedResponse
    ) : this(
        name = response.name,
        legacyId = response.legacyId,
        model = response.model,
        type = response.type,
        roles = response.roles,
        active = response.active,
        imo = response.imo,
        mmsi = response.mmsi,
        abs = response.abs,
        shipClass = response.shipClass,
        mass = MassFormatted.formatMass(response.massKg, response.massLbs),
        yearBuilt = response.yearBuilt,
        homePort = response.homePort,
        status = response.status,
        speed = response.speed,
        course = response.course,
        lat = response.lat,
        lng = response.lng,
        lastUpdate = response.lastUpdate,
        link = response.link,
        image = response.image,
        id = response.id
    )
}
