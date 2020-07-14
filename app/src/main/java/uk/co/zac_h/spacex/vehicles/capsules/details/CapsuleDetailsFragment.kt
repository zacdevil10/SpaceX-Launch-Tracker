package uk.co.zac_h.spacex.vehicles.capsules.details

import android.annotation.SuppressLint
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
import uk.co.zac_h.spacex.model.spacex.CapsulesModel

class CapsuleDetailsFragment : Fragment() {

    private var _binding: FragmentCapsuleDetailsBinding? = null
    private val binding get() = _binding!!

    private var capsule: CapsulesModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        capsule = arguments?.getParcelable("capsule") as CapsulesModel?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCapsuleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding.toolbar.setupWithNavController(navController, appBarConfig)

        capsule?.let {
            binding.capsuleDetailsConstraint.transitionName = it.id

            binding.toolbar.title = it.serial

            it.serial?.let { serial ->
                binding.capsuleDetailsTypeText.text = when {
                    serial.startsWith("C1") -> "Dragon 1.0"
                    serial.startsWith("C2") -> "Dragon 2.0"
                    else -> ""
                }
            }

            it.lastUpdate?.let { lastUpdate ->
                binding.capsuleDetailsText.text = lastUpdate
            } ?: run {
                binding.capsuleDetailsText.visibility = View.GONE
            }

            binding.capsuleDetailsStatusText.text = it.status?.capitalize()
            binding.capsuleDetailsReuseText.text = it.reuseCount.toString()
            binding.capsuleDetailsLandingText.text =
                ((it.landLandings ?: 0) + (it.waterLandings ?: 0)).toString()

            it.launches?.let { launches ->
                binding.capsuleDetailsMissionsRecycler.apply {
                    layoutManager = LinearLayoutManager(this@CapsuleDetailsFragment.context)
                    setHasFixedSize(true)
                    adapter = MissionsAdapter(context, launches)
                }
            } ?: run { binding.capsuleDetailsNoMissionLabel.visibility = View.VISIBLE }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
