package uk.co.zac_h.spacex.launches.details.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.crew.adapters.CrewAdapter
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCrewBinding
import uk.co.zac_h.spacex.dto.spacex.Crew
import uk.co.zac_h.spacex.utils.orUnknown

class LaunchDetailsCrewFragment : BaseFragment(), NetworkInterface.View<List<Crew>> {

    private lateinit var binding: FragmentLaunchDetailsCrewBinding

    private var presenter: NetworkInterface.Presenter<List<Crew>>? = null

    private lateinit var crewAdapter: CrewAdapter
    private var crew: ArrayList<Crew> = ArrayList()

    private lateinit var id: String

    companion object {
        const val CREW_KEY = "crew"
        const val ID_KEY = "id"

        @JvmStatic
        fun newInstance(id: String) = LaunchDetailsCrewFragment().apply {
            this.id = id
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            id = it.getString(ID_KEY).orUnknown()
            //crew = it.getParcelableArrayList(CREW_KEY) ?: ArrayList()
        }
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

        crewAdapter = CrewAdapter()

        binding.launchDetailsCrewRecycler.apply {
            setHasFixedSize(true)
            adapter = crewAdapter
        }

        presenter?.getOrUpdate(crew, id)

        binding.swipeRefresh.setOnRefreshListener {

            presenter?.get(id)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //outState.putParcelableArrayList(CREW_KEY, crew)
        outState.putString(ID_KEY, id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun update(response: List<Crew>) {
        /*if (apiState != ApiResult.Status.SUCCESS) binding.launchDetailsCrewRecycler.layoutAnimation =
            animateLayoutFromBottom(requireContext())

        */

        crew = response as ArrayList<Crew>
        crewAdapter.update(response)
        binding.launchDetailsCrewRecycler.scheduleLayoutAnimation()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {

        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        /**/
    }
}