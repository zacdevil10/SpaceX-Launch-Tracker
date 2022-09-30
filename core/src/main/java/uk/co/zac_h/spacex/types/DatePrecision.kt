package uk.co.zac_h.spacex.types

enum class DatePrecision(val precision: String) {
    HALF("yyyy"),
    QUARTER("yyyy"),
    YEAR("yyyy"),
    MONTH("MMM yyyy"),
    DAY("dd MMM yyyy"),
    HOUR("dd MMM yy - HH:mm zzz")
}
