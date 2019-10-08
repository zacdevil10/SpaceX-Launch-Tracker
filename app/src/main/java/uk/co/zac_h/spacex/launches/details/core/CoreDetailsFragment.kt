package uk.co.zac_h.spacex.launches.details.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_core_details.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.launches.adapters.CoreMissionsAdapter
import uk.co.zac_h.spacex.model.CoreModel
import uk.co.zac_h.spacex.model.CoreSpecModel
import uk.co.zac_h.spacex.utils.setImageAndTint

class CoreDetailsFragment : Fragment(), CoreDetailsView {

    private lateinit var presenter: CoreDetailsPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_core_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!::presenter.isInitialized) presenter =
            CoreDetailsPresenterImpl(this, CoreDetailsInteractorImpl())

        val core = arguments?.getParcelable("core") as CoreSpecModel?

        core?.serial?.let {
            core_details_serial_text.text = it

            presenter.getCoreDetails(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        core_details_mission_recycler.adapter = null
    }

    override fun updateCoreMissionsList(coreModel: CoreModel) {
        coreModel.missions?.let {
            core_details_mission_recycler.apply {
                layoutManager = LinearLayoutManager(this@CoreDetailsFragment.context)
                setHasFixedSize(true)
                adapter = CoreMissionsAdapter(context, it)
            }
        }
    }

    override fun updateCoreStats(coreModel: CoreModel) {
        coreModel.apply {
            core_details_block_text.text = block
            core_details_details_text.text = details
            core_details_status_text.text = status
            core_details_reuse_text.text = reuseCount.toString()
            core_details_rtls_attempts_text.text = attemptsRtls.toString()
            core_details_rtls_landings_text.text = landingsRtls.toString()
            core_details_asds_attempts_text.text = attemptsAsds.toString()
            core_details_asds_landings_text.text = landingsAsds.toString()
            core_details_water_landing_image.apply {
                landingWater?.let { waterLanding ->
                    if (waterLanding) setImageAndTint(
                        R.drawable.ic_check_circle_black_24dp,
                        R.color.success
                    )
                    else setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
                } ?: setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
            }
        }
    }

    override fun showProgress() {
        core_details_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        core_details_progress_bar.visibility = View.INVISIBLE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
