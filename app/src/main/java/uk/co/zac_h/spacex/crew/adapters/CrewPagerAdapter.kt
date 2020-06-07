package uk.co.zac_h.spacex.crew.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.CrewModel

class CrewPagerAdapter(private val context: Context?, private val crew: List<CrewModel>) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_crew, container, false)

        var expanded = true

        val person = crew[position]

        val constraintLayout: ConstraintLayout = view.findViewById(R.id.list_item_crew_constraint)
        val imageView: ImageView = view.findViewById(R.id.list_item_crew_image)
        val name: TextView = view.findViewById(R.id.list_item_crew_title)
        val placeholder: ConstraintLayout = view.findViewById(R.id.list_item_crew_placeholder)
        val launchRecycler: RecyclerView = view.findViewById(R.id.list_item_crew_recycler)
        val status: TextView = view.findViewById(R.id.list_item_crew_status)

        constraintLayout.tag = person.id

        constraintLayout.transitionName = person.id

        Glide.with(view).load(person.image).into(imageView)

        name.text = person.name
        status.text = person.status

        launchRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CrewLaunchAdapter(context, person.launches)
        }

        constraintLayout.setOnClickListener {
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