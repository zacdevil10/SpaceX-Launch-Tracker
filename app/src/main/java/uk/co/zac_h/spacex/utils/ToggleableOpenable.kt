package uk.co.zac_h.spacex.utils

import androidx.customview.widget.Openable

abstract class ToggleableOpenable : Openable {

    fun toggle() {
        if (isOpen) close() else open()
    }

}