package uk.co.zac_h.spacex.launches.details.ships

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsShipsBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchDetailsShipsAdapter
import uk.co.zac_h.spacex.model.spacex.Ship
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchDetailsShipsFragment : Fragment(), LaunchDetailsShipsContract.View,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentLaunchDetailsShipsBinding? = null

    private var presenter: LaunchDetailsShipsContract.Presenter? = null

    private lateinit var shipsAdapter: LaunchDetailsShipsAdapter
    private lateinit var shipsArray: ArrayList<Ship>

    private var id: String? = null

    companion object {
        @JvmStatic
        fun newInstance(args: Any) = LaunchDetailsShipsFragment().apply {
            arguments = bundleOf("id" to args)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shipsArray =
            savedInstanceState?.getParcelableArrayList("ships") ?: ArrayList()
        id = arguments?.getString("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchDetailsShipsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        presenter = LaunchDetailsShipsPresenter(this, LaunchDetailsShipsInteractor())

        shipsAdapter = LaunchDetailsShipsAdapter(shipsArray)

        binding?.launchDetailsShipsRecycler?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = shipsAdapter
        }

        if (shipsArray.isEmpty()) id?.let {
            presenter?.getShips(it)
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
        outState.putParcelableArrayList("ships", shipsArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        binding = null
    }

    override fun updateShipsRecyclerView(ships: List<Ship>) {
        shipsArray.clear()
        shipsArray.addAll(ships)

        shipsAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        binding?.launchDetailsShipsProgress?.show()
    }

    override fun hideProgress() {
        binding?.launchDetailsShipsProgress?.hide()
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (shipsArray.isEmpty()) presenter?.getShips(it)
            }
        }
    }
}