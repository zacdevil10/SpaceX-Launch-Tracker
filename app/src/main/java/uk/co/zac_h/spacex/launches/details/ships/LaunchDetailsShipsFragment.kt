package uk.co.zac_h.spacex.launches.details.ships

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsShipsBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchDetailsShipsAdapter
import uk.co.zac_h.spacex.model.spacex.Ship
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom

class LaunchDetailsShipsFragment : BaseFragment(), NetworkInterface.View<List<Ship>> {

    override var title: String = "Launch Details Ships"

    private var binding: FragmentLaunchDetailsShipsBinding? = null

    private var presenter: NetworkInterface.Presenter<Nothing>? = null

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
    ): View = FragmentLaunchDetailsShipsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

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
            presenter?.get(it)
        }
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

    override fun update(response: List<Ship>) {
        shipsArray.clear()
        shipsArray.addAll(response)

        binding?.launchDetailsShipsRecycler?.layoutAnimation = animateLayoutFromBottom(context)
        shipsAdapter.notifyDataSetChanged()
        binding?.launchDetailsShipsRecycler?.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding?.launchDetailsShipsProgress?.show()
    }

    override fun hideProgress() {
        binding?.launchDetailsShipsProgress?.hide()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (shipsArray.isEmpty()) presenter?.get(it)
            }
        }
    }
}