package uk.co.zac_h.spacex.about.company

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentCompanyBinding
import uk.co.zac_h.spacex.model.spacex.Company
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class CompanyFragment : Fragment(), CompanyContract.CompanyView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentCompanyBinding? = null

    private var presenter: CompanyContract.CompanyPresenter? = null

    private var companyInfo: Company? = null

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
        binding = FragmentCompanyBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

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
        binding = null
    }

    override fun updateCompanyInfo(company: Company) {
        companyInfo = company
        binding?.apply {
            company.headquarters?.let {
                companyAddressText.text =
                    context?.getString(R.string.address, it.address, it.city, it.state)
            }

            companyWebsiteButton.setOnClickListener { openWebLink(company.website) }
            companyTwitterButton.setOnClickListener { openWebLink(company.twitter) }
            companyAlbumButton.setOnClickListener { openWebLink(company.flickr) }

            companySummaryText.text = company.summary
            companyFoundedText.text =
                context?.getString(R.string.founded, company.founder, company.founded)
            companyCeoText.text = company.ceo
            companyCtoText.text = company.cto
            companyCooText.text = company.coo
            companyCtoProText.text = company.ctoPropulsion
            companyValuationText.text = company.valuation
            companyEmployeesText.text = company.employees.toString()
            companyVehiclesText.text = company.vehicles.toString()
            companyLaunchSitesText.text = company.launchSites.toString()
            companyTestSitesText.text = company.testSites.toString()
        }
    }

    fun openWebLink(link: String?) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
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
            if (companyInfo == null) presenter?.getCompanyInfo()
        }
    }

}
