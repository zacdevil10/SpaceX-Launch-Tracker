package uk.co.zac_h.spacex.launches.details.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.crew.CrewView
import uk.co.zac_h.spacex.crew.adapters.CrewAdapter
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCrewBinding
import uk.co.zac_h.spacex.model.spacex.Crew

class LaunchDetailsCrewFragment : BaseFragment(), CrewView {

    override var title: String = "Launch Details Crew"

    private var binding: FragmentLaunchDetailsCrewBinding? = null

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
        binding = this
    }.root

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
        binding = null
    }

    override fun update(response: List<Crew>) {
        crew.clear()
        crew.addAll(response)

        crewAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        binding?.progress?.show()
    }

    override fun hideProgress() {
        binding?.progress?.hide()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (crew.isEmpty()) presenter?.get(it)
            }
        }
    }
}