package uk.co.zac_h.spacex.launches

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import uk.co.zac_h.spacex.launches.details.LaunchDetailsContainerContract
import uk.co.zac_h.spacex.launches.details.LaunchDetailsContainerPresenter

class LaunchDetailsContainerTest {

    private lateinit var mPresenter: LaunchDetailsContainerContract.Presenter

    @Mock
    val mView: LaunchDetailsContainerContract.View =
        mock(LaunchDetailsContainerContract.View::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        mPresenter = LaunchDetailsContainerPresenter(mView)
    }

    @Test
    fun updateCountdownTime() {
        mPresenter.updateCountdown(3382968)

        verify(mView).updateCountdown("T-00:00:56:22")
    }

    @Test
    fun hideCountdownWhenTbd() {
        mPresenter.startCountdown(0, true)

        verify(mView).hideCountdown()
    }

    @Test
    fun hideCountdownWhenTbdIsNull() {
        mPresenter.startCountdown(0, null)

        verify(mView).hideCountdown()
    }

}