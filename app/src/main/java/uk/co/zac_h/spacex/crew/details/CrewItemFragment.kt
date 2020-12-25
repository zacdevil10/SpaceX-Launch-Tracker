package uk.co.zac_h.spacex.crew.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetBehavior
import uk.co.zac_h.spacex.crew.adapters.CrewLaunchAdapter
import uk.co.zac_h.spacex.databinding.FragmentCrewItemBinding
import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.model.spacex.CrewStatus
import uk.co.zac_h.spacex.utils.SPACEX_CREW_STATUS_ACTIVE
import uk.co.zac_h.spacex.utils.SPACEX_CREW_STATUS_INACTIVE
import uk.co.zac_h.spacex.utils.SPACEX_CREW_STATUS_RETIRED
import uk.co.zac_h.spacex.utils.SPACEX_CREW_STATUS_UNKNOWN
import java.util.*
import kotlin.math.roundToInt

class CrewItemFragment : Fragment() {

    private var binding: FragmentCrewItemBinding? = null

    companion object {
        fun newInstance(crew: Crew): Fragment {
            return CrewItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("crew", crew)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCrewItemBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val person = arguments?.getParcelable<Crew>("crew")

        binding?.apply {
            itemCrewConstraint.transitionName = person?.id

            val typedVal = TypedValue()
            val initialMargin = itemCrewIndicator.marginTop
            val bottomSheetBehavior = BottomSheetBehavior.from(itemCrewBottomSheet)

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
                                context!!.resources.displayMetrics
                            )

                            itemCrewIndicator.layoutParams =
                                (itemCrewIndicator.layoutParams as ConstraintLayout.LayoutParams).apply {
                                    topMargin =
                                        (initialMargin + (actionBarHeight * slideOffset)).roundToInt()
                                }

                        }
                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {

                    }
                })
            }

            Glide.with(view).load(person?.image).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    parentFragment?.startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    parentFragment?.startPostponedEnterTransition()
                    return false
                }
            }).into(itemCrewImage)

            itemCrewTitle.text = person?.name
            person?.status?.let { status ->
                itemCrewStatus.text = when (status) {
                    CrewStatus.ACTIVE -> SPACEX_CREW_STATUS_ACTIVE
                    CrewStatus.INACTIVE -> SPACEX_CREW_STATUS_INACTIVE
                    CrewStatus.RETIRED -> SPACEX_CREW_STATUS_RETIRED
                    CrewStatus.UNKNOWN -> SPACEX_CREW_STATUS_UNKNOWN
                }.capitalize(Locale.getDefault())
            }
            itemCrewAgencyText.text = person?.agency

            person?.launches?.let {
                itemCrewRecycler.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = CrewLaunchAdapter(context, it)
                }
                if (it.isEmpty()) itemCrewMissionLabel.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}