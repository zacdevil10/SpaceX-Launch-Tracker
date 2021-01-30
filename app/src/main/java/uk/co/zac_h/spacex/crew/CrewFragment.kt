package uk.co.zac_h.spacex.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.crew.adapters.CrewAdapter
import uk.co.zac_h.spacex.databinding.FragmentCrewBinding
import uk.co.zac_h.spacex.model.spacex.Crew

class CrewFragment : BaseFragment(), CrewView {

    companion object {
        const val CREW_KEY = "crew"
    }

    override var title: String = "Crew"

    private var binding: FragmentCrewBinding? = null

    private var presenter: NetworkInterface.Presenter<List<Crew>?>? = null

    private lateinit var crewAdapter: CrewAdapter
    private lateinit var crewArray: ArrayList<Crew>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crewArray = savedInstanceState?.getParcelableArrayList(CREW_KEY) ?: ArrayList()
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

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        presenter = CrewPresenterImpl(this, CrewInteractorImpl())

        crewAdapter = CrewAdapter(this, crewArray)

        binding?.crewRecycler?.apply {
            setHasFixedSize(true)
            adapter = crewAdapter
        }

        prepareTransitions()
        postponeEnterTransition()

        binding?.swipeRefresh?.setOnRefreshListener {
            presenter?.get()
        }

        if (crewArray.isEmpty()) presenter?.get()
    }

    private fun prepareTransitions() {
        sharedElementReturnTransition = MaterialContainerTransform()

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val selectedViewHolder: RecyclerView.ViewHolder =
                    binding?.crewRecycler?.findViewHolderForAdapterPosition(MainActivity.currentPosition)
                        ?: return

                names?.get(0)?.let { name ->
                    selectedViewHolder.itemView.let { itemView ->
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(CREW_KEY, crewArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        binding = null
    }

    override fun update(response: List<Crew>) {
        crewArray.clear()
        crewArray.addAll(response)

        crewAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        binding?.progress?.show()
    }

    override fun hideProgress() {
        binding?.progress?.hide()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding?.swipeRefresh?.isRefreshing = isRefreshing
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (crewArray.isEmpty() || it.progress.isShown) presenter?.get()
            }
        }
    }
}