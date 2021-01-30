package uk.co.zac_h.spacex.launches.details.cores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCoresBinding
import uk.co.zac_h.spacex.launches.adapters.FirstStageAdapter
import uk.co.zac_h.spacex.model.spacex.LaunchCore
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom

class LaunchDetailsCoresFragment : BaseFragment(), NetworkInterface.View<List<LaunchCore>> {

    override var title: String = "Launch Details Cores"

    private var binding: FragmentLaunchDetailsCoresBinding? = null

    private var presenter: NetworkInterface.Presenter<Nothing>? = null

    private lateinit var coresAdapter: FirstStageAdapter
    private lateinit var cores: ArrayList<LaunchCore>

    private var id: String? = null

    companion object {
        const val CORES_KEY = "cores"
        const val ID_KEY = "id"

        @JvmStatic
        fun newInstance(args: Any) = LaunchDetailsCoresFragment().apply {
            arguments = bundleOf(ID_KEY to args)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cores = savedInstanceState?.getParcelableArrayList(CORES_KEY) ?: ArrayList()
        id = arguments?.getString(ID_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsCoresBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        presenter = LaunchDetailsCoresPresenter(this, LaunchDetailsCoresInteractor())

        coresAdapter = FirstStageAdapter(cores)

        binding?.launchDetailsCoresRecycler?.apply {
            layoutManager = LinearLayoutManager(this@LaunchDetailsCoresFragment.context)
            adapter = coresAdapter
        }

        if (cores.isEmpty()) id?.let {
            presenter?.get(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(CORES_KEY, cores)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        binding = null
    }

    override fun update(response: List<LaunchCore>) {
        cores.clear()
        cores.addAll(response)

        binding?.launchDetailsCoresRecycler?.layoutAnimation = animateLayoutFromBottom(context)
        coresAdapter.notifyDataSetChanged()
        binding?.launchDetailsCoresRecycler?.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding?.progress?.show()
    }

    override fun hideProgress() {
        binding?.progress?.hide()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (cores.isEmpty()) presenter?.get(it)
            }
        }
    }
}