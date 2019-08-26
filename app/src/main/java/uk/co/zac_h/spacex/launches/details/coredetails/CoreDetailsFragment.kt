package uk.co.zac_h.spacex.launches.details.coredetails

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
import uk.co.zac_h.spacex.utils.data.CoreMissionsModel
import uk.co.zac_h.spacex.utils.data.CoreModel
import uk.co.zac_h.spacex.utils.data.CoreSpecModel

class CoreDetailsFragment : Fragment(), CoreDetailsView {

    private lateinit var presenter: CoreDetailsPresenter

    private lateinit var missionsAdapter: CoreMissionsAdapter
    private var missionsList = ArrayList<CoreMissionsModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_core_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CoreDetailsPresenterImpl(this, CoreDetailsInteractorImpl())

        missionsAdapter = CoreMissionsAdapter(context, missionsList)

        val core = arguments?.getParcelable("core") as CoreSpecModel?

        core?.let {
            core_details_serial_text.text = it.serial

            if (missionsList.isEmpty()) presenter.getCoreDetails(it.serial)
        }

        core_details_mission_recycler.apply {
            layoutManager = LinearLayoutManager(this@CoreDetailsFragment.context)
            setHasFixedSize(true)
            adapter = missionsAdapter
        }
    }

    override fun updateCoreMissionsList(coreModel: CoreModel?) {
        coreModel?.missions?.let {
            missionsList.addAll(it)
        }
        missionsAdapter.notifyDataSetChanged()
    }

    override fun toggleProgress(visibility: Int) {
        core_details_mission_progress_bar.visibility = visibility
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
