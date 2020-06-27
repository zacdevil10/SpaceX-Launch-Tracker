package uk.co.zac_h.spacex.utils.views

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class HeaderItemDecoration(
    recyclerView: RecyclerView,
    private val isHeader: (itemPosition: Int) -> Boolean,
    private val isHorizontal: Boolean
) : RecyclerView.ItemDecoration() {

    private var currentHeader: Pair<Int, RecyclerView.ViewHolder>? = null

    init {
        recyclerView.adapter?.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                // clear saved header as it can be outdated now
                currentHeader = null
            }
        })

        recyclerView.doOnEachNextLayout {
            // clear saved layout as it may need layout update
            currentHeader = null
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val topChild = parent.getChildAt(0) ?: return
        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }

        val headerView = getHeaderViewForItem(topChildPosition, parent) ?: return

        val contactPoint = if (isHorizontal) headerView.right else headerView.bottom
        val childInContact = getChildInContact(parent, contactPoint) ?: return

        if (isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeader(c, headerView, childInContact)
            return
        }

        drawHeader(c, headerView)
    }

    private fun getHeaderViewForItem(itemPosition: Int, parent: RecyclerView): View? {
        parent.adapter?.let {
            val headerPosition = getHeaderPositionForItem(itemPosition)
            val headerType = it.getItemViewType(headerPosition)
            // if match reuse viewHolder
            if (currentHeader?.first == headerPosition && currentHeader?.second?.itemViewType == headerType) {
                return currentHeader?.second?.itemView
            }

            val headerHolder = it.createViewHolder(parent, headerType)
            it.onBindViewHolder(headerHolder, headerPosition)
            fixLayoutSize(parent, headerHolder.itemView)
            // save for next draw
            currentHeader = headerPosition to headerHolder

            return headerHolder.itemView
        } ?: return null
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        if (isHorizontal) {
            c.translate((nextHeader.left - currentHeader.width).toFloat(), 0f)
        } else {
            c.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
        }
        currentHeader.draw(c)
        c.restore()
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        parent.children.forEach {
            val bounds = Rect()
            parent.getDecoratedBoundsWithMargins(it, bounds)
            if (isHorizontal) {
                if (bounds.right > contactPoint && bounds.left <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = it
                    return@forEach
                }
            } else {
                if (bounds.bottom > contactPoint && bounds.top <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = it
                    return@forEach
                }
            }
        }
        return childInContact
    }

    /**
     * Properly measures and layouts the top sticky header.
     *
     * @param parent ViewGroup: RecyclerView in this case.
     */
    private fun fixLayoutSize(parent: ViewGroup, view: View) {

        // Specs for recyclerView (RecyclerView)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // Specs for children (headers)
        val childWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    private fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = 0
        var currentPosition = itemPosition
        do {
            if (isHeader(currentPosition)) {
                headerPosition = currentPosition
                break
            }
            currentPosition -= 1
        } while (currentPosition >= 0)
        return headerPosition
    }
}

inline fun View.doOnEachNextLayout(crossinline action: (view: View) -> Unit) {
    addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
        action(
            view
        )
    }
}