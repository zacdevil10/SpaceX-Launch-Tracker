package uk.co.zac_h.spacex.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.crew.adapters.CrewAdapter
import uk.co.zac_h.spacex.databinding.FragmentCrewBinding
import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.utils.ApiResult
import uk.co.zac_h.spacex.utils.Keys.CrewKeys
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.utils.clearAndAdd

class CrewFragment : BaseFragment(), NetworkInterface.View<List<Crew>> {

    override val title: String by lazy { getString(R.string.menu_crew) }

    private lateinit var binding: FragmentCrewBinding

    private var presenter: NetworkInterface.Presenter<List<Crew>?>? = null

    private lateinit var crewAdapter: CrewAdapter
    private lateinit var crewArray: ArrayList<Crew>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crewArray =
            savedInstanceState?.getParcelableArrayList(CrewKeys.CREW_SAVED_STATE) ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCrewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            binding.root.doOnPreDraw { startPostponedEnterTransition() }
        }

        binding.toolbarLayout.toolbar.setup()

        presenter = CrewPresenterImpl(this, CrewInteractorImpl())

        crewAdapter =
            CrewAdapter(crewArray) { binding.root.doOnPreDraw { startPostponedEnterTransition() } }

        binding.crewRecycler.apply {
            setHasFixedSize(true)
            adapter = crewAdapter
        }

        prepareTransitions()
        postponeEnterTransition()

        binding.swipeRefresh.setOnRefreshListener {
            apiState = ApiResult.Status.PENDING
            presenter?.get()
        }

        if (crewArray.isEmpty()) presenter?.get() else apiState = ApiResult.Status.SUCCESS
    }

    private fun prepareTransitions() {
        sharedElementReturnTransition = MaterialContainerTransform()

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val selectedViewHolder: RecyclerView.ViewHolder =
                    binding.crewRecycler.findViewHolderForAdapterPosition(MainActivity.currentPosition)
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

    override fun onResume() {
        super.onResume()
        if (apiState == ApiResult.Status.SUCCESS) hideProgress()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(CrewKeys.CREW_SAVED_STATE, crewArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun update(response: List<Crew>) {
        apiState = ApiResult.Status.SUCCESS

        crewArray.clearAndAdd(response)
        binding.crewRecycler.layoutAnimation = animateLayoutFromBottom(requireContext())
        crewAdapter.update(response)
        binding.crewRecycler.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding.toolbarLayout.progress.show()
    }

    override fun hideProgress() {
        binding.toolbarLayout.progress.hide()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        apiState = ApiResult.Status.FAILURE
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiResult.Status.PENDING, ApiResult.Status.FAILURE -> presenter?.get()
            ApiResult.Status.SUCCESS -> {
            }
        }
    }
}