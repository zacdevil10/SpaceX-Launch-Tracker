package uk.co.zac_h.spacex.vehicles.cores.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentCoreDetailsBinding
import uk.co.zac_h.spacex.launches.adapters.CoreMissionsAdapter
import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.utils.setImageAndTint

class CoreDetailsFragment : Fragment(), CoreDetailsContract.CoreDetailsView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentCoreDetailsBinding? = null
    private val binding get() = _binding!!

    private var presenter: CoreDetailsContract.CoreDetailsPresenter? = null

    private var core: CoreModel? = null
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = context?.let { MaterialContainerTransform(it) }

        core = if (savedInstanceState != null) {
            savedInstanceState.getParcelable("core")
        } else {
            arguments?.getParcelable("core")
        }
        id = arguments?.getString("core_id")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoreDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CoreDetailsPresenterImpl(this, CoreDetailsInteractorImpl())

        core?.let {
            presenter?.addCoreModel(it)
        } ?: id?.let {
            presenter?.getCoreDetails(it)
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
        outState.putParcelable("core", core)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        _binding = null
    }

    override fun updateCoreDetails(coreModel: CoreModel) {
        coreModel.apply {
            core = coreModel

            binding.coreDetailsScrollview.transitionName = serial

            binding.coreDetailsSerialText.text = serial
            binding.coreDetailsBlockText.text = block ?: "TBD"
            binding.coreDetailsDetailsText.text = details
            binding.coreDetailsStatusText.text = status
            binding.coreDetailsReuseText.text = reuseCount.toString()
            binding.coreDetailsRtlsAttemptsText.text = attemptsRtls.toString()
            binding.coreDetailsRtlsLandingsText.text = landingsRtls.toString()
            binding.coreDetailsAsdsAttemptsText.text = attemptsAsds.toString()
            binding.coreDetailsAsdsLandingsText.text = landingsAsds.toString()
            binding.coreDetailsWaterLandingImage.apply {
                landingWater?.let { waterLanding ->
                    if (waterLanding) setImageAndTint(
                        R.drawable.ic_check_circle_black_24dp,
                        R.color.success
                    )
                    else setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
                } ?: setImageAndTint(R.drawable.ic_remove_circle_black_24dp, R.color.failed)
            }
        }

        coreModel.missions?.let {
            binding.coreDetailsMissionRecycler.apply {
                layoutManager = LinearLayoutManager(this@CoreDetailsFragment.context)
                setHasFixedSize(true)
                adapter = CoreMissionsAdapter(context, it)
            }
        }
    }

    override fun showProgress() {
        binding.coreDetailsProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.coreDetailsProgressBar.visibility = View.INVISIBLE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            id?.let {
                if (core == null) presenter?.getCoreDetails(it)
            }
        }
    }
}
