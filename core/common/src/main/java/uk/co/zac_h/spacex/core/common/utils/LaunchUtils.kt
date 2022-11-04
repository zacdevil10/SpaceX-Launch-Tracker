package uk.co.zac_h.spacex.core.common.utils

fun List<String>.formatCustomers(): String {
    var customers = ""

    for (i in 0..this.size.minus(1)) {
        customers += if (i != this.size.minus(1)) "${this[i]}, " else this[i]
    }

    return customers
}
