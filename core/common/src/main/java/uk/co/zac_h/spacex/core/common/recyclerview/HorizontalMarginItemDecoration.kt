package uk.co.zac_h.spacex.core.common.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class HorizontalMarginItemDecoration(private val space: Int) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        println(state.itemCount)
        println(parent.getChildAdapterPosition(view))

        with(outRect) {
            if (state.itemCount > 1) {
                if (parent.getChildAdapterPosition(view) != state.itemCount - 1) {
                    right = space
                } else {
                    left = space
                }
            }
        }
    }
}