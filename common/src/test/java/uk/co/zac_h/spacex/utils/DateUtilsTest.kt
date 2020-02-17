package uk.co.zac_h.spacex.utils

import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateUtilsTest {

    private val dateInMillis: Long = 1143239400

    @Test
    fun formatMillisecondDateToLongFormatString() {
        val tbdDateFormatted = dateInMillis.formatDateMillisLong(true)
        val confirmedDateFormatted = dateInMillis.formatDateMillisLong()

        assert(
            tbdDateFormatted == SimpleDateFormat(
                "MMM yyyy - HH:mm zzz",
                Locale.ENGLISH
            ).apply { timeZone = TimeZone.getDefault() }.format(Date(dateInMillis.times(1000L)))
        )

        assert(
            confirmedDateFormatted == SimpleDateFormat(
                "dd MMM yyyy - HH:mm zzz",
                Locale.ENGLISH
            ).apply { timeZone = TimeZone.getDefault() }.format(Date(dateInMillis.times(1000L)))
        )
    }

    @Test
    fun formatMillisecondDateToShortFormatString() {
        val tbdDateFormatted = dateInMillis.formatDateMillisShort(true)
        val confirmedDateFormatted = dateInMillis.formatDateMillisShort()

        assert(
            tbdDateFormatted == SimpleDateFormat(
                "MMM yy - HH:mm",
                Locale.ENGLISH
            ).apply { timeZone = TimeZone.getDefault() }.format(Date(dateInMillis.times(1000L)))
        )

        assert(
            confirmedDateFormatted == SimpleDateFormat(
                "dd MMM yy - HH:mm",
                Locale.ENGLISH
            ).apply { timeZone = TimeZone.getDefault() }.format(Date(dateInMillis.times(1000L)))
        )
    }

    @Test
    fun formatMillisecondDateToDayMonthString() {
        assert(
            dateInMillis.formatDateMillisDDMMM() == SimpleDateFormat(
                "dd MMM",
                Locale.ENGLISH
            ).apply { timeZone = TimeZone.getDefault() }.format(Date(dateInMillis.times(1000L)))
        )
    }

    @Test
    fun formatMillisecondDateToYearString() {
        assert(
            dateInMillis.formatDateMillisYYYY() == SimpleDateFormat(
                "YYYY",
                Locale.ENGLISH
            ).apply {
                timeZone = TimeZone.getDefault()
            }.format(Date(dateInMillis.times(1000L))).toInt()
        )
    }

    @Test
    fun convertDateStringToMilliseconds() {
        val dateString = "Wed Jan 29 22:08:52 +0000 2020"
        val invalidDateString = "Wed Jan 29"

        assert(dateString.dateStringToMillis() == 1580335732000L)
        assert(invalidDateString.dateStringToMillis() == 0L)
    }

    @Test
    fun convertDateMillisecondsToTimeSince() {
        val fiveHoursAgo = System.currentTimeMillis() - 18000000
        val futureDate = System.currentTimeMillis() + 1000000

        assert(fiveHoursAgo.convertDate() == "5h")
        assert(futureDate.convertDate() == "")
    }

    @Test
    fun showStringForTime_whenTimeIsLessThan48Hours() {
        assert(getString(200, 2 * MINUTE_MILLIS, Date(0), Date(0)) == "1m")
        assert(getString(200, 90 * MINUTE_MILLIS, Date(0), Date(0)) == "1h")
        assert(getString(200, 36 * HOUR_MILLIS, Date(0), Date(0)) == "1d")
    }

    @Test
    fun showStringForTime_whenTimeIsMoreThan48Hours() {
        assert(
            getString(
                200,
                200 * HOUR_MILLIS,
                Date(1548027798000),
                Date(1562676092358)
            ) == "20 Jan 19"
        )
    }

    companion object {
        private const val SECOND_MILLIS: Long = 1000
        private const val MINUTE_MILLIS: Long = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS: Long = 60 * MINUTE_MILLIS
    }
}