package uk.co.zac_h.spacex.launches.details.payloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsPayloadsBinding
import uk.co.zac_h.spacex.launches.adapters.PayloadAdapter
import uk.co.zac_h.spacex.model.spacex.PayloadModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchDetailsPayloadsFragment : Fragment(), LaunchDetailsPayloadsContract.View,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentLaunchDetailsPayloadsBinding? = null
    private val binding get() = _binding!!

    private var presenter: LaunchDetailsPayloadsContract.Presenter? = null

    private lateinit var payloadAdapter: PayloadAdapter
    private lateinit var payloads: ArrayList<PayloadModel>

    private var id: String? = null

    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            LaunchDetailsPayloadsFragment().apply {
                arguments = Bundle().apply {
                    putString("id", id)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        payloads =
            savedInstanceState?.getParcelableArrayList<PayloadModel>("payloads") ?: ArrayList()
        id = arguments?.getString("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchDetailsPayloadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchDetailsPayloadsPresenter(this, LaunchDetailsPayloadsInteractor())

        payloadAdapter = PayloadAdapter(context, payloads)

        binding.launchDetailsPayloadRecycler.apply {
            layoutManager = LinearLayoutManager(this@LaunchDetailsPayloadsFragment.context)
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
        _binding = null
    }

    override fun updatePayloadsRecyclerView(payloadsList: List<PayloadModel>?) {
        payloadsList?.let {
            payloads.clear()
            payloads.addAll(it)

            payloadAdapter.notifyDataSetChanged()
        }
    }

    override fun showProgress() {
        binding.launchDetailsPayloadProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.launchDetailsPayloadProgress.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (payloads.isEmpty()) presenter?.getLaunch(it)
            }
        }
    }
}