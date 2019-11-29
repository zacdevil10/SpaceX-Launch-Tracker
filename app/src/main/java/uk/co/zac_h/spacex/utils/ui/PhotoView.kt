package uk.co.zac_h.spacex.utils.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_photo_view.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.twitter.TweetMediaModel

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PhotoView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_view)

        val media = intent.getParcelableArrayListExtra<TweetMediaModel>("media")

        photo_view_pager.apply {
            adapter = PhotoViewPagerAdapter(context, media)
            currentItem = intent.extras?.get("position") as Int? ?: 0
        }
    }
}
