package uk.co.zac_h.spacex.launches.details.payloads

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsPayloadsBinding
import uk.co.zac_h.spacex.launches.adapters.PayloadAdapter
import uk.co.zac_h.spacex.model.spacex.Payload
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.clearAndAdd

class LaunchDetailsPayloadsFragment : BaseFragment(), NetworkInterface.View<List<Payload>> {

    private lateinit var binding: FragmentLaunchDetailsPayloadsBinding

    private var presenter: NetworkInterface.Presenter<Nothing>? = null

    private lateinit var payloadAdapter: PayloadAdapter
    private lateinit var payloads: ArrayList<Payload>

    private lateinit var id: String

    companion object {
        const val PAYLOADS_KEY = "payloads"

        @JvmStatic
        fun newInstance(id: String) = LaunchDetailsPayloadsFragment().apply {
            this.id = id
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        payloads = savedInstanceState?.getParcelableArrayList(PAYLOADS_KEY) ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsPayloadsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchDetailsPayloadsPresenter(this, LaunchDetailsPayloadsInteractor())

        payloadAdapter = PayloadAdapter(requireContext(), payloads)

        binding.launchDetailsPayloadRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = payloadAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        binding.swipeRefresh.setOnRefreshListener {
            apiState = ApiState.PENDING
            presenter?.get(id)
        }

        if (payloads.isEmpty()) presenter?.get(id)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(PAYLOADS_KEY, payloads)
        super.onSaveInstanceState(outState)
    }

    override fun update(response: List<Payload>) {
        apiState = ApiState.SUCCESS

        payloads.clearAndAdd(response)
        payloadAdapter.notifyDataSetChanged()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiState.PENDING, ApiState.FAILED -> presenter?.get(id)
            ApiState.SUCCESS -> Log.i(title, "Network available and data loaded")
        }
    }
}