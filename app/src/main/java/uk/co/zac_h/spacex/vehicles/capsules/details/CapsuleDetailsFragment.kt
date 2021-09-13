package uk.co.zac_h.spacex.vehicles.capsules.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentCapsuleDetailsBinding
import uk.co.zac_h.spacex.dto.spacex.Capsule
import uk.co.zac_h.spacex.launches.adapters.MissionsAdapter
import uk.co.zac_h.spacex.utils.*
import uk.co.zac_h.spacex.vehicles.capsules.CapsulesViewModel
import java.util.*

class CapsuleDetailsFragment : BaseFragment() {

    override val title: String by lazy { navArgs.label ?: title }

    private val navArgs: CapsuleDetailsFragmentArgs by navArgs()

    private val viewModel: CapsulesViewModel by viewModels()

    private lateinit var binding: FragmentCapsuleDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCapsuleDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.get()

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.toolbarLayout.toolbar.setup()

        viewModel.capsules.observe(viewLifecycleOwner) { result ->
            update(result.data?.first { it.id == navArgs.id })
        }
    }

    private fun update(capsule: Capsule?) {
        with(binding) {
            toolbarLayout.toolbar.setup()

            capsuleDetailsConstraint.transitionName = capsule?.id

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
            } ?: run { capsuleDetailsMissionLabel.visibility = View.GONE }

            toolbarLayout.progress.hide()
        }
    }
}
