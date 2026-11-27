package uk.co.zac_h.spacex.core.model

/** What a first stage is on its rocket, as Launch Library names it. */
enum class CoreType(val type: String) {
    CORE("Core"),
    BOOSTER("Strap-On Booster"),
    OTHER("Other");

    companion object {
        /** The type with this Launch Library name, or [OTHER]. */
        fun fromApiName(name: String?): CoreType =
            entries.firstOrNull { it != OTHER && it.type == name } ?: OTHER
    }
}
