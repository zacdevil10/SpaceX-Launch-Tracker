package uk.co.zac_h.spacex.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class CrewFragment : Fragment(), CrewContract.CrewView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentCrewBinding? = null

    private var presenter: CrewContract.CrewPresenter? = null

    private lateinit var crewAdapter: CrewAdapter
    private lateinit var crewArray: ArrayList<Crew>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crewArray = savedInstanceState?.getParcelableArrayList("crew") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCrewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

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

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        presenter = CrewPresenterImpl(this, CrewInteractorImpl())

        crewAdapter = CrewAdapter(this, crewArray)

        binding?.crewRecycler?.apply {
            setHasFixedSize(true)
            adapter = crewAdapter
        }

        prepareTransitions()
        postponeEnterTransition()

        binding?.crewSwipeRefresh?.setOnRefreshListener {
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
                    binding?.crewRecycler?.findViewHolderForAdapterPosition(MainActivity.currentPosition)
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
        binding?.root?.doOnPreDraw { startPostponedEnterTransition() }
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
        binding = null
    }

    override fun updateCrew(crew: List<Crew>) {
        crewArray.clear()
        crewArray.addAll(crew)

        crewAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        binding?.crewProgressBar?.show()
    }

    override fun hideProgress() {
        binding?.crewProgressBar?.hide()
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        binding?.crewSwipeRefresh?.isRefreshing = refreshing
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (crewArray.isEmpty() || it.crewProgressBar.isShown) presenter?.getCrew()
            }
        }
    }
}