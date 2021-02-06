package uk.co.zac_h.spacex.about.company

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentCompanyBinding
import uk.co.zac_h.spacex.model.spacex.Company

class CompanyFragment : BaseFragment(), NetworkInterface.View<Company> {

    companion object {
        const val COMPANY_KEY = "company_info"
    }

    override var title: String = "Company"

    private var binding: FragmentCompanyBinding? = null

    private var presenter: NetworkInterface.Presenter<Company?>? = null

    private var companyInfo: Company? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            companyInfo = it.getParcelable(COMPANY_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCompanyBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        presenter = CompanyPresenterImpl(this, CompanyInteractorImpl())

        presenter?.getOrUpdate(companyInfo)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        companyInfo?.let {
            outState.putParcelable(COMPANY_KEY, it)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        binding = null
    }

    override fun update(response: Company) {
        companyInfo = response
        binding?.apply {
            response.headquarters?.let {
                companyAddress.text =
                    context?.getString(R.string.address, it.address, it.city, it.state)
            }

            companyWebsite.setOnClickListener { openWebLink(response.website) }
            companyTwitter.setOnClickListener { openWebLink(response.twitter) }
            companyAlbum.setOnClickListener { openWebLink(response.flickr) }

            companySummary.text = response.summary
            companyFounded.text =
                context?.getString(R.string.founded, response.founder, response.founded)
            companyCeo.text = response.ceo
            companyCto.text = response.cto
            companyCoo.text = response.coo
            companyCtoPro.text = response.ctoPropulsion
            companyValuation.text = response.valuation
            companyEmployees.text = response.employees.toString()
            companyVehicles.text = response.vehicles.toString()
            companyLaunchSites.text = response.launchSites.toString()
            companyTestSites.text = response.testSites.toString()
        }
    }

    fun openWebLink(link: String?) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    override fun showProgress() {
        binding?.progress?.show()
    }

    override fun hideProgress() {
        binding?.progress?.hide()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (companyInfo == null) presenter?.getOrUpdate(null)
        }
    }

}
