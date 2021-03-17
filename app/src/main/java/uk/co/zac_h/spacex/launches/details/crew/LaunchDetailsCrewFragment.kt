package uk.co.zac_h.spacex.launches.details.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.crew.adapters.CrewAdapter
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCrewBinding
import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.clearAndAdd

class LaunchDetailsCrewFragment : BaseFragment(), NetworkInterface.View<List<Crew>> {

    override var title: String = "Launch Details Crew"

    private var _binding: FragmentLaunchDetailsCrewBinding? = null
    private val binding get() = _binding!!

    private var presenter: NetworkInterface.Presenter<Nothing>? = null

    private lateinit var crewAdapter: CrewAdapter
    private lateinit var crew: ArrayList<Crew>

    private var id: String? = null

    companion object {
        const val CREW_KEY = "crew"
        const val ID_KEY = "id"

        @JvmStatic
        fun newInstance(args: Any) = LaunchDetailsCrewFragment().apply {
            arguments = bundleOf(ID_KEY to args)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crew = savedInstanceState?.getParcelableArrayList(CREW_KEY) ?: ArrayList()
        id = arguments?.getString(ID_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsCrewBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        presenter = LaunchDetailsCrewPresenter(this, LaunchDetailsCrewInteractor())

        crewAdapter = CrewAdapter(crew)

        binding.launchDetailsCrewRecycler.apply {
            setHasFixedSize(true)
            adapter = crewAdapter
        }

        if (crew.isEmpty()) id?.let {
            presenter?.get(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(CREW_KEY, crew)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        _binding = null
    }

    override fun update(response: List<Crew>) {
        apiState = ApiState.SUCCESS

        crew.clearAndAdd(response)
        crewAdapter.notifyDataSetChanged()
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
    }

    override fun networkAvailable() {
        when(apiState) {
            ApiState.PENDING, ApiState.FAILED -> id?.let { presenter?.get(it) }
            ApiState.SUCCESS -> {}
        }
    }
}