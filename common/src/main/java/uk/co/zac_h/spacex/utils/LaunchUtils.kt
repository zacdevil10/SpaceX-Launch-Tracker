package uk.co.zac_h.spacex.utils

import uk.co.zac_h.spacex.model.CoreSpecModel

fun List<CoreSpecModel>.formatBlockNumber(): String {
    var blockText = ""

    this.forEach { i ->
        if (i.block == null) {
            blockText = "TBD "
            return@forEach
        }
        blockText += "${i.block} "
    }

    return blockText.dropLast(1).replace(" ", " | ")
}

fun List<String>.formatCustomers(): String {
    var customers = ""

    this.forEach { i ->
        customers += "$i, "
    }

    return customers.dropLast(2)
}