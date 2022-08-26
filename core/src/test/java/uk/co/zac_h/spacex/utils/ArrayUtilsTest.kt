package uk.co.zac_h.spacex.utils

import org.junit.Test

class ArrayUtilsTest {

    private val arrayList = ArrayList<String>().apply {
        add("Included 1")
        add("Excluded 1")
        add("Included 2")
    }

    private val excludedArray = ArrayList<String>()

    @Test
    fun addItemsToArrayListExcludingASingleItem() {
        excludedArray.addAllExcludingPosition(
            arrayList,
            EXCLUDED_POSITION
        )

        assert(!excludedArray.contains(arrayList[EXCLUDED_POSITION]))
    }

    companion object {
        const val EXCLUDED_POSITION = 1
    }

}