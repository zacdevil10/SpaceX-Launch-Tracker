package uk.co.zac_h.spacex.launches.details.payloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsPayloadsBinding
import uk.co.zac_h.spacex.launches.adapters.PayloadAdapter
import uk.co.zac_h.spacex.model.spacex.Payload
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchDetailsPayloadsFragment : Fragment(), LaunchDetailsPayloadsContract.View,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentLaunchDetailsPayloadsBinding? = null

    private var presenter: LaunchDetailsPayloadsContract.Presenter? = null

    private lateinit var payloadAdapter: PayloadAdapter
    private lateinit var payloads: ArrayList<Payload>

    private var id: String? = null

    companion object {
        @JvmStatic
        fun newInstance(args: Any) = LaunchDetailsPayloadsFragment().apply {
            arguments = bundleOf("id" to args)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        payloads =
            savedInstanceState?.getParcelableArrayList<Payload>("payloads") ?: ArrayList()
        id = arguments?.getString("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchDetailsPayloadsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        presenter = LaunchDetailsPayloadsPresenter(this, LaunchDetailsPayloadsInteractor())

        payloadAdapter = PayloadAdapter(context, payloads)

        binding?.launchDetailsPayloadRecycler?.apply {
            layoutManager = LinearLayoutManager(this@LaunchDetailsPayloadsFragment.context)
            setHasFixedSize(true)
            adapter = payloadAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        if (payloads.isEmpty()) id?.let {
            presenter?.getLaunch(it)
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
        outState.putParcelableArrayList("payloads", payloads)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun updatePayloadsRecyclerView(payloadsList: List<Payload>?) {
        payloadsList?.let {
            payloads.clear()
            payloads.addAll(it)

            payloadAdapter.notifyDataSetChanged()
        }
    }

    override fun showProgress() {
        binding?.progressIndicator?.show()
    }

    override fun hideProgress() {
        binding?.progressIndicator?.hide()
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (payloads.isEmpty()) presenter?.getLaunch(it)
            }
        }
    }
}