package uk.co.zac_h.spacex.launches.details.crew

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.crew.adapters.CrewAdapter
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCrewBinding
import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.utils.clearAndAdd

class LaunchDetailsCrewFragment : BaseFragment(), NetworkInterface.View<List<Crew>> {

    private lateinit var binding: FragmentLaunchDetailsCrewBinding

    private var presenter: NetworkInterface.Presenter<Nothing>? = null

    private lateinit var crewAdapter: CrewAdapter
    private lateinit var crew: ArrayList<Crew>

    private lateinit var id: String

    companion object {
        const val CREW_KEY = "crew"

        @JvmStatic
        fun newInstance(id: String) = LaunchDetailsCrewFragment().apply {
            this.id = id
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        crew = savedInstanceState?.getParcelableArrayList(CREW_KEY) ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsCrewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchDetailsCrewPresenter(this, LaunchDetailsCrewInteractor())

        crewAdapter = CrewAdapter(crew)

        binding.launchDetailsCrewRecycler.apply {
            setHasFixedSize(true)
            adapter = crewAdapter
        }

        if (crew.isEmpty()) presenter?.get(id)

        binding.swipeRefresh.setOnRefreshListener {
            apiState = ApiState.PENDING
            presenter?.get(id)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(CREW_KEY, crew)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun update(response: List<Crew>) {
        apiState = ApiState.SUCCESS

        crew.clearAndAdd(response)

        binding.launchDetailsCrewRecycler.layoutAnimation = animateLayoutFromBottom(requireContext())
        crewAdapter.notifyDataSetChanged()
        binding.launchDetailsCrewRecycler.scheduleLayoutAnimation()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        when(apiState) {
            ApiState.PENDING, ApiState.FAILED -> presenter?.get(id)
            ApiState.SUCCESS -> Log.i(title, "Network available and data loaded")
        }
    }
}