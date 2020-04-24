package uk.co.zac_h.spacex.vehicles.capsules.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.databinding.FragmentCapsuleDetailsBinding
import uk.co.zac_h.spacex.model.spacex.CapsulesModel
import uk.co.zac_h.spacex.utils.formatDateMillisShort
import uk.co.zac_h.spacex.vehicles.adapters.CapsuleMissionsAdapter

class CapsuleDetailsFragment : Fragment() {

    private var _binding: FragmentCapsuleDetailsBinding? = null
    private val binding get() = _binding!!

    private var capsule: CapsulesModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = context?.let { MaterialContainerTransform(it) }

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

        capsule?.let {
            binding.capsuleDetailsScrollview.transitionName = it.serial

            binding.capsuleDetailsTypeText.text = it.type
            binding.capsuleDetailsText.text = it.details
            binding.capsuleDetailsStatusText.text = it.status.capitalize()
            binding.capsuleDetailsDateText.text =
                it.originalLaunchUnix?.formatDateMillisShort() ?: "TBD"
            binding.capsuleDetailsReuseText.text = it.reuseCount.toString()
            binding.capsuleDetailsLandingText.text = it.landings.toString()

            if (it.missions.isNotEmpty()) binding.capsuleDetailsMissionsRecycler.apply {
                layoutManager = LinearLayoutManager(this@CapsuleDetailsFragment.context)
                setHasFixedSize(true)
                adapter = CapsuleMissionsAdapter(context, it.missions)
            } else binding.capsuleDetailsNoMissionLabel.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
