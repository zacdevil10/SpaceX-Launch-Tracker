package uk.co.zac_h.spacex.utils

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*

abstract class BottomDrawerFragment : Fragment() {

    abstract val bottomSheetComponent: BottomSheetComponent

    private val behaviour: BottomSheetBehavior<CoordinatorLayout> by lazy {
        from(bottomSheetComponent.container)
    }

    private val bottomSheetCallback = BottomDrawerCallback()

    private val closeDrawerOnBackPressed = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            close()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, closeDrawerOnBackPressed)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetComponent.scrim.setOnClickListener { close() }

        bottomSheetCallback.apply {
            addOnSlideAction(AlphaSlideAction(bottomSheetComponent.scrim))
            addOnSlideAction(
                ContainerSheetTransformSlideAction(
                    bottomSheetComponent.container,
                    bottomSheetComponent.foregroundShapeDrawable
                )
            )

            addOnStateChangedAction(VisibilityStateAction(bottomSheetComponent.scrim))
            addOnStateChangedAction(object : OnStateChangedAction {
                override fun onStateChanged(sheet: View, newState: Int) {
                    closeDrawerOnBackPressed.isEnabled = newState != STATE_HIDDEN
                }
            })
        }

        behaviour.addBottomSheetCallback(bottomSheetCallback)
        behaviour.state = STATE_HIDDEN
    }

    fun toggle() = when (behaviour.state) {
        STATE_HIDDEN -> open()
        STATE_HALF_EXPANDED,
        STATE_DRAGGING,
        STATE_EXPANDED,
        STATE_COLLAPSED,
        STATE_SETTLING -> close()
        else -> false
    }

    fun open(): Boolean {
        behaviour.state = STATE_HALF_EXPANDED
        return true
    }

    fun close(): Boolean {
        behaviour.state = STATE_HIDDEN
        return false
    }

    fun addOnStateChangedAction(action: OnStateChangedAction) {
        bottomSheetCallback.addOnStateChangedAction(action)
    }

}