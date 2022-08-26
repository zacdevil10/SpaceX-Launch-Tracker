package uk.co.zac_h.spacex.utils

import org.junit.Before
import org.junit.Test

class LaunchUtilsTest {

    private val customersArray = ArrayList<String>()

    @Before
    fun setup() {
        customersArray.apply {
            add("James")
            add("Harold")
            add("Bill")
        }
    }

    @Test
    fun showStringForCustomers_whenMultipleCustomersAreGiven() {
        assert(customersArray.formatCustomers() == "James, Harold, Bill")
    }
}