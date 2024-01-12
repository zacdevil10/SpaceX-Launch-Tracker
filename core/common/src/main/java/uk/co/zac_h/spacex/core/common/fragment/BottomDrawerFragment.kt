package uk.co.zac_h.spacex.core.common.fragment

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.Openable
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HALF_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.bottomsheet.BottomSheetBehavior.from

abstract class BottomDrawerFragment : Fragment(), Openable {

    abstract val container: ConstraintLayout

    abstract val scrim: View

    private val behaviour: BottomSheetBehavior<ConstraintLayout> by lazy {
        from(container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scrim.setOnClickListener { close() }

        behaviour.state = STATE_HIDDEN
    }

    fun toggle() {
        if (isOpen) close() else open()
    }

    override fun isOpen(): Boolean = behaviour.state != STATE_HIDDEN

    override fun open() {
        behaviour.state = STATE_HALF_EXPANDED
    }

    override fun close() {
        behaviour.state = STATE_HIDDEN
    }
}
