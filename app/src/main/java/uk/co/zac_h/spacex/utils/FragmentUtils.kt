package uk.co.zac_h.spacex.utils

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

fun Fragment.openWebLink(link: String?) {
    link?.let { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link))) }
}
