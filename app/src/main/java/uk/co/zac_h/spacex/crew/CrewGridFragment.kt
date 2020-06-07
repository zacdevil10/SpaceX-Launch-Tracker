package uk.co.zac_h.spacex.crew

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.crew.adapters.CrewAdapter
import uk.co.zac_h.spacex.databinding.FragmentCrewGridBinding
import uk.co.zac_h.spacex.model.spacex.CrewModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class CrewGridFragment : Fragment(), CrewContract.CrewView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentCrewGridBinding? = null
    private val binding get() = _binding!!

    private var presenter: CrewContract.CrewPresenter? = null

    private lateinit var crewAdapter: CrewAdapter
    private lateinit var crewArray: ArrayList<CrewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        sharedElementEnterTransition = MaterialContainerTransform()

        crewArray = if (savedInstanceState != null) {
            savedInstanceState.getParcelableArrayList("crew") ?: ArrayList()
        } else {
            arguments?.getParcelableArrayList<CrewModel>("crew") as ArrayList<CrewModel>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrewGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        presenter = CrewPresenterImpl(this, CrewInteractorImpl())

        crewAdapter = CrewAdapter(crewArray)

        binding.crewRecycler.apply {
            layoutManager = GridLayoutManager(this@CrewGridFragment.context, 2)
            setHasFixedSize(true)
            adapter = crewAdapter
        }

        binding.crewSwipeRefresh.setOnRefreshListener {
            presenter?.getCrew()
        }

        if (crewArray.isEmpty()) presenter?.getCrew()
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onStop() {
        super.onStop()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_crew_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.list -> {
            findNavController().navigate(
                R.id.action_crew_grid_fragment_to_crew_page_fragment,
                bundleOf("crew" to crewArray),
                null,
                FragmentNavigatorExtras((binding.crewRecycler.findViewHolderForAdapterPosition(0)!!.itemView) to crewArray[0].id)
            )
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun updateCrew(crew: List<CrewModel>) {
        crewArray.clear()
        crewArray.addAll(crew)

        crewAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        binding.crewProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.crewProgressBar.visibility = View.GONE
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        binding.crewSwipeRefresh.isRefreshing = refreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (crewArray.isEmpty() || binding.crewProgressBar.visibility == View.VISIBLE) presenter?.getCrew()
        }
    }
}