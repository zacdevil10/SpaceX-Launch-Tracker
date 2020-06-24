package uk.co.zac_h.spacex.crew.adapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.CrewModel
import kotlin.math.roundToInt

class CrewPagerAdapter(
    private val context: Context?,
    private val crew: List<CrewModel>
) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_crew, container, false)

        val person = crew[position]

        val constraintLayout: CoordinatorLayout = view.findViewById(R.id.list_item_crew_constraint)
        val imageView: ImageView = view.findViewById(R.id.list_item_crew_image)
        val name: TextView = view.findViewById(R.id.list_item_crew_title)
        val bottomSheet: ConstraintLayout = view.findViewById(R.id.list_item_crew_details)
        val launchRecycler: RecyclerView = view.findViewById(R.id.list_item_crew_recycler)
        val status: TextView = view.findViewById(R.id.list_item_crew_status)
        val agency: TextView = view.findViewById(R.id.list_item_crew_agency_text)
        val indicator: Chip = view.findViewById(R.id.list_item_crew_indicator)

        val typedVal = TypedValue()
        val initialMargin = indicator.marginTop

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_COLLAPSED

            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    context?.theme?.resolveAttribute(
                        android.R.attr.actionBarSize,
                        typedVal,
                        true
                    )?.let {
                        val actionBarHeight = TypedValue.complexToDimensionPixelSize(
                            typedVal.data,
                            context.resources.displayMetrics
                        )

                        indicator.layoutParams =
                            (indicator.layoutParams as ConstraintLayout.LayoutParams).apply {
                                topMargin =
                                    (initialMargin + (actionBarHeight * slideOffset)).roundToInt()
                            }

                    }
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }
            })
        }

        constraintLayout.tag = person.id

        Glide.with(view).load(person.image).into(imageView)

        name.text = person.name
        status.text = person.status
        agency.text = person.agency

        launchRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CrewLaunchAdapter(context, person.launches)
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