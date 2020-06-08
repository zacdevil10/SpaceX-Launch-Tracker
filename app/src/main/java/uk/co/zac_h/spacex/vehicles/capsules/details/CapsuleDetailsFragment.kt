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
import uk.co.zac_h.spacex.vehicles.adapters.CapsuleMissionsAdapter

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

        capsule?.let {
            binding.capsuleDetailsScrollview.transitionName = it.id

            it.serial?.let { serial ->
                binding.capsuleDetailsTypeText.text = when {
                    serial.startsWith("C1") -> "Dragon 1.0"
                    serial.startsWith("C2") -> "Dragon 2.0"
                    else -> ""
                }
            }

            binding.capsuleDetailsText.text = it.lastUpdate
            binding.capsuleDetailsStatusText.text = it.status?.capitalize()
            binding.capsuleDetailsReuseText.text = it.reuseCount.toString()
            binding.capsuleDetailsLandingText.text =
                ((it.landLandings ?: 0) + (it.waterLandings ?: 0)).toString()

            if (it.launches.isNotEmpty()) binding.capsuleDetailsMissionsRecycler.apply {
                layoutManager = LinearLayoutManager(this@CapsuleDetailsFragment.context)
                setHasFixedSize(true)
                adapter = CapsuleMissionsAdapter(context, it.launches)
            } else binding.capsuleDetailsNoMissionLabel.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
