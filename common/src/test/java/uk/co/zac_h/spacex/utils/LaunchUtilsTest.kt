package uk.co.zac_h.spacex.utils

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import uk.co.zac_h.spacex.model.spacex.CoreSpecModel

class LaunchUtilsTest {

    @Mock
    private lateinit var mCoreSpecModel0: CoreSpecModel
    @Mock
    private lateinit var mCoreSpecModel1: CoreSpecModel
    @Mock
    private lateinit var mCoreSpecModel2: CoreSpecModel

    private val coreSpecListAll = ArrayList<CoreSpecModel>()
    private val coreSpecListSingle = ArrayList<CoreSpecModel>()

    private val customersArray = ArrayList<String>()

    @Before
    fun setup() {
        mCoreSpecModel0 = mock(CoreSpecModel::class.java)
        mCoreSpecModel1 = mock(CoreSpecModel::class.java)
        mCoreSpecModel2 = mock(CoreSpecModel::class.java)

        coreSpecListAll.apply {
            add(mCoreSpecModel0)
            add(mCoreSpecModel1)
            add(mCoreSpecModel2)
        }

        coreSpecListSingle.add(mCoreSpecModel0)

        customersArray.apply {
            add("James")
            add("Harold")
            add("Bill")
        }
    }

    @Test
    fun showStringForBlockNumber_whenThreeBlocksAreGiven() {
        Mockito.`when`(mCoreSpecModel0.block).thenReturn(4)
        Mockito.`when`(mCoreSpecModel1.block).thenReturn(5)
        Mockito.`when`(mCoreSpecModel2.block).thenReturn(3)

        assert(coreSpecListAll.formatBlockNumber() == "4 | 5 | 3")
    }

    @Test
    fun showStringForBlockNumber_whenOnlyTwoBlocksAreGiven() {
        Mockito.`when`(mCoreSpecModel0.block).thenReturn(4)
        Mockito.`when`(mCoreSpecModel1.block).thenReturn(null)
        Mockito.`when`(mCoreSpecModel2.block).thenReturn(3)

        assert(coreSpecListAll.formatBlockNumber() == "TBD")
    }

    @Test
    fun showStringForBlockNumber_whenOnlyOneBlocksAreGiven() {
        Mockito.`when`(mCoreSpecModel0.block).thenReturn(5)

        assert(coreSpecListSingle.formatBlockNumber() == "5")
    }

    @Test
    fun showStringForCustomers_whenMultipleCustomersAreGiven() {
        assert(customersArray.formatCustomers() == "James, Harold, Bill")
    }
}