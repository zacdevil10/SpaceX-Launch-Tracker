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
import uk.co.zac_h.spacex.databinding.FragmentCapsuleDetailsBinding
import uk.co.zac_h.spacex.launches.adapters.MissionsAdapter
import uk.co.zac_h.spacex.model.spacex.Capsule
import uk.co.zac_h.spacex.model.spacex.CapsuleStatus
import uk.co.zac_h.spacex.model.spacex.CapsuleType
import uk.co.zac_h.spacex.utils.*
import java.util.*

class CapsuleDetailsFragment : Fragment() {

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
    ): View? {
        binding = FragmentCapsuleDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        binding?.apply {
            capsuleDetailsConstraint.transitionName = capsule?.id

            toolbar.title = capsule?.serial

            capsule?.type?.let { serial ->
                capsuleDetailsTypeText.text = when (serial) {
                    CapsuleType.DRAGON_1 -> SPACEX_CAPSULE_TYPE_DRAGON_1
                    CapsuleType.DRAGON_1_1 -> SPACEX_CAPSULE_TYPE_DRAGON_1_1
                    CapsuleType.DRAGON_2 -> SPACEX_CAPSULE_TYPE_DRAGON_2
                }
            }

            capsule?.lastUpdate?.let { lastUpdate ->
                capsuleDetailsText.text = lastUpdate
            } ?: run {
                capsuleDetailsText.visibility = View.GONE
            }

            capsule?.status?.let { status ->
                capsuleDetailsStatusText.text = when (status) {
                    CapsuleStatus.UNKNOWN -> SPACEX_CAPSULE_STATUS_UNKNOWN
                    CapsuleStatus.ACTIVE -> SPACEX_CAPSULE_STATUS_ACTIVE
                    CapsuleStatus.RETIRED -> SPACEX_CAPSULE_STATUS_RETIRED
                    CapsuleStatus.DESTROYED -> SPACEX_CAPSULE_STATUS_DESTROYED
                }.capitalize(Locale.getDefault())
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
