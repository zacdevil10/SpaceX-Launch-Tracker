package uk.co.zac_h.spacex.core.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.hideKeyboard(root: View) {
    val ime = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    ime.hideSoftInputFromWindow(root.windowToken, 0)
}
