package uk.co.zac_h.spacex.about.company

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentCompanyBinding
import uk.co.zac_h.spacex.model.spacex.Company
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.Keys.CompanyKeys
import uk.co.zac_h.spacex.utils.openWebLink

class CompanyFragment : BaseFragment(), NetworkInterface.View<Company> {

    override val title: String by lazy { getString(R.string.menu_company) }

    private lateinit var binding: FragmentCompanyBinding

    private var presenter: NetworkInterface.Presenter<Company?>? = null

    private var companyInfo: Company? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        companyInfo = savedInstanceState?.getParcelable(CompanyKeys.COMPANY_INFO)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCompanyBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout.toolbar.setup()

        presenter = CompanyPresenterImpl(this, CompanyInteractorImpl())

        presenter?.getOrUpdate(companyInfo)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(CompanyKeys.COMPANY_INFO, companyInfo)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun update(response: Company) {
        apiState = ApiState.SUCCESS
        companyInfo = response
        with(binding) {
            response.headquarters?.let {
                companyAddress.text = getString(R.string.address, it.address, it.city, it.state)
            }

            response.website?.let { website ->
                companyWebsite.visibility = View.VISIBLE
                companyWebsite.setOnClickListener { openWebLink(website) }
            }
            response.twitter?.let { twitter ->
                companyTwitter.visibility = View.VISIBLE
                companyTwitter.setOnClickListener { openWebLink(twitter) }
            }
            response.flickr?.let { flickr ->
                companyAlbum.visibility = View.VISIBLE
                companyAlbum.setOnClickListener { openWebLink(flickr) }
            }

            companySummary.text = response.summary
            companyFounded.text = getString(R.string.founded, response.founder, response.founded)
            companyCeo.text = response.ceo
            companyCto.text = response.cto
            companyCoo.text = response.coo
            companyCtoPro.text = response.ctoPropulsion
            companyValuation.text = response.valuation
            companyEmployees.text = response.employees
            companyVehicles.text = response.vehicles
            companyLaunchSites.text = response.launchSites
            companyTestSites.text = response.testSites
        }
    }

    override fun showProgress() {
        binding.toolbarLayout.progress.show()
    }

    override fun hideProgress() {
        binding.toolbarLayout.progress.hide()
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiState.PENDING, ApiState.FAILED -> presenter?.getOrUpdate(companyInfo)
            ApiState.SUCCESS -> Log.i(title, "Network available and data loaded")
        }
    }

}
