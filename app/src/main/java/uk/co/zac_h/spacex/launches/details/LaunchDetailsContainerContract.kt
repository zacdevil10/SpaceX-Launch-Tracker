package uk.co.zac_h.spacex.launches.details

interface LaunchDetailsContainerContract {

    interface View {
        fun updateCountdown(countdown: String)
        fun setCountdown(time: Long)
        fun showCountdown()
        fun hideCountdown()
    }

    interface Presenter {
        fun startCountdown(launchDateUnix: Long?, tbd: Boolean?)
        fun updateCountdown(time: Long)
    }

}