package uk.co.zac_h.spacex.launches.details.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.crew.CrewContract
import uk.co.zac_h.spacex.crew.adapters.CrewAdapter
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCrewBinding
import uk.co.zac_h.spacex.model.spacex.CrewModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchDetailsCrewFragment : Fragment(), CrewContract.CrewView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentLaunchDetailsCrewBinding? = null
    private val binding get() = _binding!!

    private var presenter: LaunchDetailsCrewContract.Presenter? = null

    private lateinit var crewAdapter: CrewAdapter
    private lateinit var crewArray: ArrayList<CrewModel>

    private var id: String? = null

    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            LaunchDetailsCrewFragment().apply {
                arguments = Bundle().apply {
                    putString("id", id)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crewArray = savedInstanceState?.getParcelableArrayList<CrewModel>("crew") ?: ArrayList()
        id = arguments?.getString("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchDetailsCrewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchDetailsCrewPresenter(this, LaunchDetailsCrewInteractor())

        crewAdapter = CrewAdapter(this, crewArray)

        binding.launchDetailsCrewRecycler.apply {
            layoutManager = GridLayoutManager(this@LaunchDetailsCrewFragment.context, 2)
            setHasFixedSize(true)
            adapter = crewAdapter
        }

        if (crewArray.isEmpty()) id?.let {
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
        outState.putParcelableArrayList("crew", crewArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        _binding = null
    }

    override fun updateCrew(crew: List<CrewModel>) {
        crewArray.clear()
        crewArray.addAll(crew)

        crewAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {

    }

    override fun startTransition() {

    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (crewArray.isEmpty()) presenter?.getCrew(it)
            }
        }
    }
}