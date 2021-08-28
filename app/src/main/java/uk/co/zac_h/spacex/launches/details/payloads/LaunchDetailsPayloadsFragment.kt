package uk.co.zac_h.spacex.launches.details.payloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.dto.spacex.Payload
import uk.co.zac_h.spacex.launches.adapters.PayloadAdapter
import uk.co.zac_h.spacex.utils.orUnknown

class LaunchDetailsPayloadsFragment : BaseFragment(), NetworkInterface.View<List<Payload>> {

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private var presenter: NetworkInterface.Presenter<List<Payload>>? = null

    private lateinit var payloadAdapter: PayloadAdapter
    private var payloads: ArrayList<Payload> = ArrayList()

    private lateinit var id: String

    companion object {
        const val PAYLOADS_KEY = "payloads"
        const val ID_KEY = "id"

        @JvmStatic
        fun newInstance(id: String) = LaunchDetailsPayloadsFragment().apply {
            this.id = id
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            id = it.getString(ID_KEY).orUnknown()
            //payloads = it.getParcelableArrayList(PAYLOADS_KEY) ?: ArrayList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchDetailsPayloadsPresenter(this, LaunchDetailsPayloadsInteractor())

        payloadAdapter = PayloadAdapter(requireContext())

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = payloadAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        binding.swipeRefresh.setOnRefreshListener {

            presenter?.get(id)
        }

        presenter?.getOrUpdate(payloads, id)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //outState.putParcelableArrayList(PAYLOADS_KEY, payloads)
        outState.putString(ID_KEY, id)
    }

    override fun update(response: List<Payload>) {
        //

        payloads = response as ArrayList<Payload>
        payloadAdapter.update(response)
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showProgress() {
        binding.progress.show()
    }

    override fun hideProgress() {
        binding.progress.hide()
    }

    override fun showError(error: String) {

        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        /**/
    }
}