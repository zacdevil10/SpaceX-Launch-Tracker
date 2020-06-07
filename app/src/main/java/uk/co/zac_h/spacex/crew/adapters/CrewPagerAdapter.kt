package uk.co.zac_h.spacex.crew.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.CrewModel

class CrewPagerAdapter(private val context: Context?, private val crew: List<CrewModel>) :
    PagerAdapter() {

    private var expanded = false

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_crew, container, false)

        val person = crew[position]

        val card: MaterialCardView = view.findViewById(R.id.list_item_crew_card)
        val imageView: ImageView = view.findViewById(R.id.list_item_crew_image)
        val name: TextView = view.findViewById(R.id.list_item_crew_title)
        val placeholder: TextView = view.findViewById(R.id.list_item_crew_placeholder)

        card.transitionName = person.id

        Glide.with(view).load(person.image).into(imageView)

        name.text = person.name

        card.setOnClickListener {
            if (expanded) {
                expanded = false

                placeholder.visibility = View.GONE

                placeholder.startAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.item_animation_to_bottom
                    )
                )
            } else {
                expanded = true

                placeholder.visibility = View.VISIBLE

                placeholder.startAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.item_animation_from_bottom
                    )
                )
            }
        }

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = `object` == view

    override fun getCount(): Int = crew.size

}