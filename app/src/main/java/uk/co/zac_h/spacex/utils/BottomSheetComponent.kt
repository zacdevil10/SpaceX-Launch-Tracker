package uk.co.zac_h.spacex.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.shape.MaterialShapeDrawable
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ComponentBottomSheetBinding

class BottomSheetComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    var binding: ComponentBottomSheetBinding =
        ComponentBottomSheetBinding.inflate(LayoutInflater.from(context)).also {
            addView(it.root)
        }

    val foregroundShapeDrawable: MaterialShapeDrawable by lazy {
        val containerContext = container.context
        MaterialShapeDrawable(
            containerContext,
            null,
            R.attr.bottomSheetStyle,
            0
        ).apply {
            fillColor = ColorStateList.valueOf(
                ContextCompat.getColor(containerContext, R.color.color_background)
            )
            elevation = resources.getDimension(R.dimen.card_elevation)
            initializeElevationOverlay(context)
        }
    }

    val scrim: View by lazy { binding.scrim }

    val container: CoordinatorLayout by lazy { binding.container }

    init {
        container.background = foregroundShapeDrawable
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child?.id == R.id.coordinator || child?.id == R.id.scrim || child?.id == R.id.container) {
            super.addView(child, index, params)
        } else {
            container.addView(child, index, params)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

}