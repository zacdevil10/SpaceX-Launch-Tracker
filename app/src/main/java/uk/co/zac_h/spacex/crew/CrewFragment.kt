package uk.co.zac_h.spacex.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.crew.adapters.CrewAdapter
import uk.co.zac_h.spacex.databinding.FragmentCrewBinding
import uk.co.zac_h.spacex.model.spacex.CrewModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class CrewFragment : Fragment(), CrewContract.CrewView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentCrewBinding? = null
    private val binding get() = _binding!!

    private var presenter: CrewContract.CrewPresenter? = null

    private lateinit var crewAdapter: CrewAdapter
    private lateinit var crewArray: ArrayList<CrewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*crewArray = when {
            savedInstanceState != null -> {
                savedInstanceState.getParcelableArrayList("crew") ?: ArrayList()
            }
            arguments != null -> {
                requireArguments().getParcelableArrayList<CrewModel>("crew") as ArrayList<CrewModel>
            }
            else -> {
                ArrayList()
            }
        }*/

        crewArray = savedInstanceState?.getParcelableArrayList<CrewModel>("crew") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        if (savedInstanceState == null) {
            startTransition()
        }

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout)
                .build()

        binding.toolbar.setupWithNavController(navController, appBarConfig)

        presenter = CrewPresenterImpl(this, CrewInteractorImpl())

        crewAdapter = CrewAdapter(this, crewArray)

        binding.crewRecycler.apply {
            setHasFixedSize(true)
            adapter = crewAdapter
        }

        prepareTransitions()
        postponeEnterTransition()

        binding.crewSwipeRefresh.setOnRefreshListener {
            presenter?.getCrew()
        }

        if (crewArray.isEmpty()) presenter?.getCrew()
    }

    private fun prepareTransitions() {
        sharedElementReturnTransition = MaterialContainerTransform()

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val selectedViewHolder: RecyclerView.ViewHolder? =
                    binding.crewRecycler.findViewHolderForAdapterPosition(MainActivity.currentPosition)
                        ?: return

                names?.get(0)?.let { name ->
                    selectedViewHolder?.itemView?.let { itemView ->
                        sharedElements?.put(
                            name,
                            itemView.findViewById(R.id.grid_item_crew_constraint)
                        )
                    }
                }
            }
        })
    }

    override fun startTransition() {
        _binding?.root?.doOnPreDraw { startPostponedEnterTransition() }
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

    override fun updateCrew(crew: List<CrewModel>) {
        crewArray.clear()
        crewArray.addAll(crew)

        crewAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        binding.crewProgressBar.show()
    }

    override fun hideProgress() {
        binding.crewProgressBar.hide()
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        binding.crewSwipeRefresh.isRefreshing = refreshing
    }

    override fun showError(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (crewArray.isEmpty() || binding.crewProgressBar.isShown) presenter?.getCrew()
        }
    }
}