package uk.co.zac_h.spacex.vehicles.rockets.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.FragmentRocketDetailsBinding
import uk.co.zac_h.spacex.model.spacex.RocketsModel
import uk.co.zac_h.spacex.utils.metricFormat
import uk.co.zac_h.spacex.utils.setImageAndTint
import uk.co.zac_h.spacex.vehicles.adapters.RocketPayloadAdapter
import java.text.DecimalFormat

class RocketDetailsFragment : Fragment() {

    private var _binding: FragmentRocketDetailsBinding? = null
    private val binding get() = _binding!!

    private var rocket: RocketsModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        rocket = arguments?.getParcelable("rocket") as RocketsModel?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRocketDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rocket?.let {
            binding.rocketDetailsCoordinator.transitionName = it.rocketId

            binding.rocketDetailsAppbarImage.setImageResource(
                when (it.rocketId) {
                    "falcon1" -> R.drawable.falcon1
                    "falcon9" -> R.drawable.falcon9
                    "falconheavy" -> R.drawable.falconheavy
                    "starship" -> R.drawable.starship
                    else -> R.drawable.starship //TODO: Add error image.
                }
            )

            binding.rocketDetailsNameText.text = it.rocketName
            binding.rocketDetailsText.text = it.description

            when (it.active) {
                true -> binding.rocketDetailsStatusImage.setImageAndTint(
                    R.drawable.ic_check_circle_black_24dp,
                    R.color.success
                )
                false -> binding.rocketDetailsStatusImage.setImageAndTint(
                    R.drawable.ic_remove_circle_black_24dp,
                    R.color.failed
                )
            }

            binding.rocketDetailsCostText.text =
                DecimalFormat("$#,###.00").format(it.costPerLaunch).toString()
            binding.rocketDetailsSuccessText.text =
                context?.getString(R.string.percentage, it.successRate)
            binding.rocketDetailsFirstFlightText.text = it.firstFlight
            binding.rocketDetailsStagesText.text = it.stages.toString()

            binding.rocketDetailsHeightText.text = context?.getString(
                R.string.measurements,
                it.height.meters.metricFormat(),
                it.height.feet.metricFormat()
            )
            binding.rocketDetailsDiameterText.text = context?.getString(
                R.string.measurements,
                it.diameter.meters.metricFormat(),
                it.diameter.feet.metricFormat()
            )
            binding.rocketDetailsMassText.text =
                context?.getString(
                    R.string.mass_formatted,
                    it.mass.kg.metricFormat(),
                    it.mass.lb.metricFormat()
                )

            with(it.firstStage) {
                when (reusable) {
                    true -> binding.rocketDetailsReusableImage.setImageAndTint(
                        R.drawable.ic_check_circle_black_24dp,
                        R.color.success
                    )
                    false -> binding.rocketDetailsReusableImage.setImageAndTint(
                        R.drawable.ic_remove_circle_black_24dp,
                        R.color.failed
                    )
                }

                binding.rocketDetailsEnginesFirstText.text = engines.toString()
                binding.rocketDetailsFuelFirstText.text = context?.getString(
                    R.string.ton_format,
                    fuelAmountTons.metricFormat()
                )
                binding.rocketDetailsBurnFirstText.text =
                    context?.getString(R.string.seconds_format, burnTimeSec ?: 0)
                binding.rocketDetailsThrustSeaText.text = context?.getString(
                    R.string.thrust,
                    thrustSeaLevel.kN.metricFormat(),
                    thrustSeaLevel.lbf.metricFormat()
                )
                binding.rocketDetailsThrustVacText.text = context?.getString(
                    R.string.thrust,
                    thrustVacuum.kN.metricFormat(),
                    thrustVacuum.lbf.metricFormat()
                )
            }

            with(it.secondStage) {
                binding.rocketDetailsEnginesSecondText.text = engines.toString()
                binding.rocketDetailsFuelSecondText.text =
                    context?.getString(R.string.ton_format, fuelAmountTons.metricFormat())
                binding.rocketDetailsBurnSecondText.text =
                    context?.getString(R.string.seconds_format, burnTimeSec ?: 0)
                binding.rocketDetailsThrustSecondText.text = context?.getString(
                    R.string.thrust,
                    thrust.kN.metricFormat(),
                    thrust.lbf.metricFormat()
                )
            }

            binding.rocketDetailsPayloadRecycler.apply {
                layoutManager = LinearLayoutManager(this@RocketDetailsFragment.context)
                setHasFixedSize(true)
                adapter =
                    RocketPayloadAdapter(this@RocketDetailsFragment.context, it.payloadWeightsList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
