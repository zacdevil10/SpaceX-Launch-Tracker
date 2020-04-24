package uk.co.zac_h.spacex.about.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentCompanyBinding
import uk.co.zac_h.spacex.model.spacex.CompanyModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import java.text.DecimalFormat

class CompanyFragment : Fragment(), CompanyContract.CompanyView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentCompanyBinding? = null
    private val binding get() = _binding!!

    private var presenter: CompanyContract.CompanyPresenter? = null

    private var companyInfo: CompanyModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            companyInfo = it.getParcelable("info")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CompanyPresenterImpl(this, CompanyInteractorImpl())

        companyInfo?.let {
            presenter?.getCompanyInfo(it)
        } ?: presenter?.getCompanyInfo()
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
        companyInfo?.let {
            outState.putParcelable("info", it)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        _binding = null
    }

    override fun updateCompanyInfo(companyModel: CompanyModel) {
        companyInfo = companyModel
        binding.apply {
            with(companyModel.headquarters) {
                binding.companyAddressText.text =
                    context?.getString(R.string.address, address, city, state)
            }
            binding.companySummaryText.text = companyModel.summary
            binding.companyFoundedText.text =
                context?.getString(R.string.founded, companyModel.founder, companyModel.founded)
            companyCeoText.text = companyModel.ceo
            companyCtoText.text = companyModel.cto
            companyCooText.text = companyModel.coo
            companyCtoProText.text = companyModel.ctoPropulsion
            companyValuationText.text =
                DecimalFormat("#,###.00").format(companyModel.valuation).toString()
            companyEmployeesText.text = companyModel.employees.toString()
            companyVehiclesText.text = companyModel.vehicles.toString()
            companyLaunchSitesText.text = companyModel.launchSites.toString()
            companyTestSitesText.text = companyModel.testSites.toString()
        }
    }

    override fun showProgress() {
        binding.companyProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.companyProgressBar.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (companyInfo == null) presenter?.getCompanyInfo()
        }
    }

}
