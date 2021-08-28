package uk.co.zac_h.spacex.vehicles.rockets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentRocketBinding
import uk.co.zac_h.spacex.dto.spacex.Rocket
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.vehicles.adapters.RocketsAdapter

class RocketFragment : BaseFragment(), NetworkInterface.View<List<Rocket>> {

    override var title: String = "Rockets"

    private lateinit var binding: FragmentRocketBinding

    private var presenter: NetworkInterface.Presenter<Nothing>? = null
    private lateinit var rocketsAdapter: RocketsAdapter

    private lateinit var rocketsArray: ArrayList<Rocket>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //rocketsArray = savedInstanceState?.getParcelableArrayList("rockets") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRocketBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = RocketPresenterImpl(this, RocketInteractorImpl())

        rocketsAdapter = RocketsAdapter(rocketsArray)

        binding.rocketRecycler.apply {
            layoutManager = LinearLayoutManager(this@RocketFragment.context)
            setHasFixedSize(true)
            adapter = rocketsAdapter
        }

        binding.rocketSwipeRefresh.setOnRefreshListener {

            presenter?.get()
        }

        if (rocketsArray.isEmpty()) presenter?.get()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //outState.putParcelableArrayList("rockets", rocketsArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun update(response: List<Rocket>) {


        rocketsArray.clear()
        rocketsArray.addAll(response)

        binding.rocketRecycler.layoutAnimation = animateLayoutFromBottom(requireContext())
        rocketsAdapter.notifyDataSetChanged()
        binding.rocketRecycler.scheduleLayoutAnimation()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.rocketSwipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        /*when (apiState) {
            ApiResult.Status.PENDING, ApiResult.Status.FAILURE -> presenter?.get()
            ApiResult.Status.SUCCESS -> Log.i(title, "Network available and data loaded")
        }*/
    }
}
