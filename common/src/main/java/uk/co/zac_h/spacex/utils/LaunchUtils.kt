package uk.co.zac_h.spacex.utils

import uk.co.zac_h.spacex.model.spacex.CoreSpecModel

fun List<CoreSpecModel>.formatBlockNumber(): String {
    var blockText = ""

    for (i in 0..this.size.minus(1)) {
        if (this[i].block == null) {
            blockText = "TBD "
            break
        }
        blockText += "${this[i].block} "
    }

    return blockText.dropLast(1).replace(" ", " | ")
}

fun List<String>.formatCustomers(): String {
    var customers = ""

    for (i in 0..this.size.minus(1)) {
        customers += if (i != this.size.minus(1)) "${this[i]}, " else this[i]
    }

    return customers
}