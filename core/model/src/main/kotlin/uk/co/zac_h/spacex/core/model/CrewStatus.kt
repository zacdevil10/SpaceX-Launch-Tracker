package uk.co.zac_h.spacex.core.model

/** An astronaut's status, as Launch Library names it. */
enum class CrewStatus(val status: String) {
    IN_TRAINING("In-Training"),
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    RETIRED("Retired"),
    DECEASED("Deceased"),
    LOST_IN_TRAINING("Lost In Training"),
    UNKNOWN("Unknown");

    companion object {
        /** The status with this Launch Library name, or [UNKNOWN]. */
        fun fromApiName(name: String?): CrewStatus =
            entries.firstOrNull { it != UNKNOWN && it.status == name } ?: UNKNOWN
    }
}
