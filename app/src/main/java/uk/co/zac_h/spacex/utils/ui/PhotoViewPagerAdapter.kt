package uk.co.zac_h.spacex.utils.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.twitter.TweetMediaModel
import uk.co.zac_h.spacex.utils.ZoomableImageView

class PhotoViewPagerAdapter(
    private val context: Context,
    private val media: ArrayList<TweetMediaModel>
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view =
            LayoutInflater.from(context).inflate(R.layout.photo_view_zoomable, container, false)

        val imageView: ZoomableImageView = view.findViewById(R.id.zoomable_image_view)
        Picasso.get().load(getImageAt(position)).into(imageView)

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = `object` == view

    override fun getCount(): Int = media.size

    private fun getImageAt(position: Int): String = media[position].url
}