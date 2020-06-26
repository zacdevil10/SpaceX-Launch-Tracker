package uk.co.zac_h.spacex.vehicles

import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import uk.co.zac_h.spacex.model.spacex.CoreExtendedModel
import uk.co.zac_h.spacex.vehicles.cores.details.CoreDetailsContract
import uk.co.zac_h.spacex.vehicles.cores.details.CoreDetailsPresenterImpl

class CoreDetailsTest {

    private lateinit var mPresenter: CoreDetailsContract.CoreDetailsPresenter

    @Mock
    val mView: CoreDetailsContract.CoreDetailsView =
        mock(CoreDetailsContract.CoreDetailsView::class.java)

    @Mock
    val mCoreModel: CoreExtendedModel = mock(CoreExtendedModel::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        mPresenter = CoreDetailsPresenterImpl(mView)
    }

    @Test
    fun `When add core model then add to view`() {
        mPresenter.addCoreModel(mCoreModel)

        verify(mView).updateCoreDetails(mCoreModel)
    }

}