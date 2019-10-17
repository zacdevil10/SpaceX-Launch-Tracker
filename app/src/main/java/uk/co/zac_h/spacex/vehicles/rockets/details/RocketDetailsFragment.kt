package uk.co.zac_h.spacex.vehicles.rockets.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_rocket_details.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.RocketsModel
import uk.co.zac_h.spacex.utils.metricFormat
import uk.co.zac_h.spacex.utils.setImageAndTint
import uk.co.zac_h.spacex.vehicles.adapters.RocketPayloadAdapter
import java.text.DecimalFormat

class RocketDetailsFragment : Fragment() {

    private var rocket: RocketsModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rocket = arguments?.getParcelable("rocket") as RocketsModel?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rocket_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rocket?.let {
            rocket_details_name_text.text = it.rocketName
            rocket_details_text.text = it.description

            when (it.active) {
                true -> rocket_details_status_image.setImageAndTint(
                    R.drawable.ic_check_circle_black_24dp,
                    R.color.success
                )
                false -> rocket_details_status_image.setImageAndTint(
                    R.drawable.ic_remove_circle_black_24dp,
                    R.color.failed
                )
            }

            rocket_details_cost_text.text =
                DecimalFormat("$#,###.00").format(it.costPerLaunch).toString()
            rocket_details_success_text.text =
                context?.getString(R.string.percentage, it.successRate)
            rocket_details_first_flight_text.text = it.firstFlight
            rocket_details_stages_text.text = it.stages.toString()

            rocket_details_height_text.text = context?.getString(
                R.string.measurements,
                it.height.meters.metricFormat(),
                it.height.feet.metricFormat()
            )
            rocket_details_diameter_text.text = context?.getString(
                R.string.measurements,
                it.diameter.meters.metricFormat(),
                it.diameter.feet.metricFormat()
            )
            rocket_details_mass_text.text =
                context?.getString(
                    R.string.mass_formatted,
                    it.mass.kg.metricFormat(),
                    it.mass.lb.metricFormat()
                )

            with(it.firstStage) {
                when (reusable) {
                    true -> rocket_details_reusable_image.setImageAndTint(
                        R.drawable.ic_check_circle_black_24dp,
                        R.color.success
                    )
                    false -> rocket_details_reusable_image.setImageAndTint(
                        R.drawable.ic_remove_circle_black_24dp,
                        R.color.failed
                    )
                }

                rocket_details_engines_first_text.text = engines.toString()
                rocket_details_fuel_first_text.text = context?.getString(
                    R.string.ton_format,
                    fuelAmountTons.metricFormat()
                )
                rocket_details_burn_first_text.text =
                    context?.getString(R.string.seconds_format, burnTimeSec ?: 0)
                rocket_details_thrust_sea_text.text = context?.getString(
                    R.string.thrust,
                    thrustSeaLevel.kN.metricFormat(),
                    thrustSeaLevel.lbf.metricFormat()
                )
                rocket_details_thrust_vac_text.text = context?.getString(
                    R.string.thrust,
                    thrustVacuum.kN.metricFormat(),
                    thrustVacuum.lbf.metricFormat()
                )
            }

            with(it.secondStage) {
                rocket_details_engines_second_text.text = engines.toString()
                rocket_details_fuel_second_text.text =
                    context?.getString(R.string.ton_format, fuelAmountTons.metricFormat())
                rocket_details_burn_second_text.text =
                    context?.getString(R.string.seconds_format, burnTimeSec ?: 0)
                rocket_details_thrust_second_text.text = context?.getString(
                    R.string.thrust,
                    thrust.kN.metricFormat(),
                    thrust.lbf.metricFormat()
                )
            }

            rocket_details_payload_recycler.apply {
                layoutManager = LinearLayoutManager(this@RocketDetailsFragment.context)
                setHasFixedSize(true)
                adapter =
                    RocketPayloadAdapter(this@RocketDetailsFragment.context, it.payloadWeightsList)
            }
        }
    }
}
