package uk.co.zac_h.spacex.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(
    var layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    abstract fun isScrollUpVisible(): Boolean

    abstract fun onScrollTop()

    abstract fun onScrolledDown()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        with(layoutManager) {
            if (findFirstCompletelyVisibleItemPosition() == 0) onScrollTop()

            if (dy > 0 && !isScrollUpVisible() && findFirstVisibleItemPosition() > 15) {
                onScrolledDown()
            }
        }
    }
}