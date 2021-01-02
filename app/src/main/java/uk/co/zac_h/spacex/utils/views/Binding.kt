package uk.co.zac_h.spacex.utils.views

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R

@BindingAdapter("loadMissionPatch")
fun loadMissionPatch(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load(url)
        .error(ContextCompat.getDrawable(view.context, R.drawable.ic_mission_patch))
        .fallback(ContextCompat.getDrawable(view.context, R.drawable.ic_mission_patch))
        .placeholder(ContextCompat.getDrawable(view.context, R.drawable.ic_mission_patch))
        .into(view)
}

@BindingAdapter("isRefreshing")
fun isRefreshing(view: SwipeRefreshLayout, refreshing: Boolean) {
    view.isRefreshing = refreshing
}