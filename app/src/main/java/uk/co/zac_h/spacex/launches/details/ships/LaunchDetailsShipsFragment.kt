package uk.co.zac_h.spacex.launches.details.ships

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsShipsBinding
import uk.co.zac_h.spacex.launches.adapters.ShipsAdapter
import uk.co.zac_h.spacex.model.spacex.ShipModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchDetailsShipsFragment : Fragment(), LaunchDetailsShipsContract.View,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentLaunchDetailsShipsBinding? = null
    private val binding get() = _binding!!

    private var presenter: LaunchDetailsShipsContract.Presenter? = null

    private lateinit var shipsAdapter: ShipsAdapter
    private lateinit var shipsArray: ArrayList<ShipModel>

    private var id: String? = null

    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            LaunchDetailsShipsFragment().apply {
                arguments = Bundle().apply {
                    putString("id", id)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shipsArray = savedInstanceState?.getParcelableArrayList<ShipModel>("ships") ?: ArrayList()
        id = arguments?.getString("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchDetailsShipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchDetailsShipsPresenter(this, LaunchDetailsShipsInteractor())

        shipsAdapter = ShipsAdapter(shipsArray)

        binding.launchDetailsShipsRecycler.apply {
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
        _binding = null
    }

    override fun updateShipsRecyclerView(ships: List<ShipModel>) {
        shipsArray.clear()
        shipsArray.addAll(ships)

        shipsAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        binding.launchDetailsShipsProgress.show()
    }

    override fun hideProgress() {
        binding.launchDetailsShipsProgress.hide()
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (shipsArray.isEmpty()) presenter?.getShips(it)
            }
        }
    }
}