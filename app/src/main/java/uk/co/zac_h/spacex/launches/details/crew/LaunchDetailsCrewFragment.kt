package uk.co.zac_h.spacex.launches.details.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.crew.CrewContract
import uk.co.zac_h.spacex.crew.adapters.CrewAdapter
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCrewBinding
import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchDetailsCrewFragment : Fragment(), CrewContract.CrewView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentLaunchDetailsCrewBinding? = null

    private var presenter: LaunchDetailsCrewContract.Presenter? = null

    private lateinit var crewAdapter: CrewAdapter
    private lateinit var crew: ArrayList<Crew>

    private var id: String? = null

    companion object {
        @JvmStatic
        fun newInstance(args: Any) = LaunchDetailsCrewFragment().apply {
            arguments = bundleOf("id" to args)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crew = savedInstanceState?.getParcelableArrayList<Crew>("crew") ?: ArrayList()
        id = arguments?.getString("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchDetailsCrewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        presenter = LaunchDetailsCrewPresenter(this, LaunchDetailsCrewInteractor())

        crewAdapter = CrewAdapter(this, crew)

        binding?.launchDetailsCrewRecycler?.apply {
            setHasFixedSize(true)
            adapter = crewAdapter
        }

        if (crew.isEmpty()) id?.let {
            presenter?.getCrew(it)
        }
    }

    override fun onResume() {
        super.onResume()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("crew", crew)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        binding = null
    }

    override fun updateCrew(crew: List<Crew>) {
        this.crew.clear()
        this.crew.addAll(crew)

        crewAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        binding?.launchDetailsCrewProgress?.show()
    }

    override fun hideProgress() {
        binding?.launchDetailsCrewProgress?.hide()
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {

    }

    override fun startTransition() {

    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (crew.isEmpty()) presenter?.getCrew(it)
            }
        }
    }
}