package uk.co.zac_h.spacex.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(var layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

    abstract fun isScrollUpVisible(): Boolean

    abstract fun loadItems()

    abstract fun onScrollTop()

    abstract fun onScrolledDown()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
            onScrollTop()
        }

        if (dy > 0 && !isScrollUpVisible() && firstVisibleItemPosition > 15) {
            onScrolledDown()
        }

        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadItems()
            }
        }
    }

}