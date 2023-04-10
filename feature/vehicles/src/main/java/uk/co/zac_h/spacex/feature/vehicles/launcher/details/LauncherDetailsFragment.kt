package uk.co.zac_h.spacex.feature.vehicles.launcher.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialSharedAxis
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.image.setImageAndTint
import uk.co.zac_h.spacex.feature.vehicles.R
import uk.co.zac_h.spacex.feature.vehicles.databinding.FragmentLauncherDetailsBinding
import uk.co.zac_h.spacex.feature.vehicles.launcher.LauncherViewModel

class LauncherDetailsFragment : BaseFragment() {

    private val viewModel: LauncherViewModel by navGraphViewModels(R.id.vehicles_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLauncherDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLauncherDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            with(viewModel.selectedLauncher?.imageUrl) {
                launcherImage.isVisible = this != null

                Glide.with(requireContext())
                    .load(this)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(launcherImage)
            }

            toolbar.title = viewModel.selectedLauncher?.serial

            launcherDetailsText.text = viewModel.selectedLauncher?.details

            if (viewModel.selectedLauncher?.status == "active") {
                launcherDetailsStatusImage.setImageAndTint(
                    R.drawable.ic_check_circle_black_24dp,
                    R.color.success
                )
            } else {
                launcherDetailsStatusImage.setImageAndTint(
                    R.drawable.ic_remove_circle_black_24dp,
                    R.color.failed
                )
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}