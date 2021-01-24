package uk.co.zac_h.spacex.vehicles.capsules.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentCapsuleDetailsBinding
import uk.co.zac_h.spacex.launches.adapters.MissionsAdapter
import uk.co.zac_h.spacex.model.spacex.Capsule
import uk.co.zac_h.spacex.utils.*
import java.util.*

class CapsuleDetailsFragment : BaseFragment() {

    override var title: String = ""

    private var binding: FragmentCapsuleDetailsBinding? = null

    private var capsule: Capsule? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        capsule = arguments?.getParcelable("capsule") as Capsule?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCapsuleDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        binding?.apply {
            capsuleDetailsConstraint.transitionName = capsule?.id

            toolbar.title = capsule?.serial

            capsule?.type?.let {
                capsuleDetailsTypeText.text = it.type
            }

            capsule?.lastUpdate?.let { lastUpdate ->
                capsuleDetailsText.text = lastUpdate
            } ?: run {
                capsuleDetailsText.visibility = View.GONE
            }

            capsule?.status?.let {
                capsuleDetailsStatusText.text = it.status
            }
            capsuleDetailsReuseText.text = capsule?.reuseCount.toString()
            capsuleDetailsLandingText.text =
                ((capsule?.landLandings ?: 0) + (capsule?.waterLandings ?: 0)).toString()

            capsule?.launches?.let { launches ->
                capsuleDetailsMissionsRecycler.apply {
                    layoutManager = LinearLayoutManager(this@CapsuleDetailsFragment.context)
                    setHasFixedSize(true)
                    adapter = MissionsAdapter(context, launches)
                }
            } ?: run { capsuleDetailsNoMissionLabel.visibility = View.VISIBLE }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
