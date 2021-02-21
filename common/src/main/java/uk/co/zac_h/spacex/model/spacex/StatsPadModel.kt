package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatsPadModel(
    val name: String?,
    val attempts: Int,
    val successes: Int,
    val status: PadStatus?,
    val type: String? = "RTLS",
    val isHeading: Boolean = false
) : Parcelable