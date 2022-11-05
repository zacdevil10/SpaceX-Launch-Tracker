package uk.co.zac_h.spacex.core.common.fragment

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.Openable
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import uk.co.zac_h.spacex.core.common.bottomsheet.*

abstract class BottomDrawerFragment : Fragment(), Openable {

    abstract val container: ConstraintLayout

    abstract val scrim: View

    private val behaviour: BottomSheetBehavior<ConstraintLayout> by lazy {
        from(container)
    }

    private val bottomSheetCallback = BottomDrawerCallback()

    private val closeDrawerOnBackPressed by lazy {
        BottomSheetBackPressed(behaviour)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scrim.setOnClickListener { close() }

        bottomSheetCallback.apply {
            addOnSlideAction(AlphaSlideAction(scrim))

            addOnStateChangedAction(VisibilityStateAction(scrim))
            addOnStateChangedAction(BackPressedStateAction(closeDrawerOnBackPressed))
        }

        behaviour.addBottomSheetCallback(bottomSheetCallback)
        behaviour.state = STATE_HIDDEN

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            closeDrawerOnBackPressed
        )
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

    fun addOnStateChangedAction(action: OnStateChangedAction) {
        bottomSheetCallback.addOnStateChangedAction(action)
    }
}
