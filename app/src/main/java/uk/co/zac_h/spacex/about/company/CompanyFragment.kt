package uk.co.zac_h.spacex.about.company

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentCompanyBinding
import uk.co.zac_h.spacex.model.spacex.Company
import uk.co.zac_h.spacex.utils.ApiState

class CompanyFragment : BaseFragment(), NetworkInterface.View<Company> {

    override val title: String by lazy { requireContext().getString(R.string.menu_company) }

    private lateinit var binding: FragmentCompanyBinding

    private var presenter: NetworkInterface.Presenter<Company?>? = null

    private var companyInfo: Company? = null

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

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun update(response: Company) {
        apiState = ApiState.SUCCESS
        companyInfo = response
        with(binding) {
            response.headquarters?.let {
                companyAddress.text =
                    requireContext().getString(R.string.address, it.address, it.city, it.state)
            }

            companyWebsite.setOnClickListener { openWebLink(response.website) }
            companyTwitter.setOnClickListener { openWebLink(response.twitter) }
            companyAlbum.setOnClickListener { openWebLink(response.flickr) }

            companySummary.text = response.summary
            companyFounded.text =
                requireContext().getString(R.string.founded, response.founder, response.founded)
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
        binding.toolbarLayout.progress.show()
    }

    override fun hideProgress() {
        binding.toolbarLayout.progress.hide()
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiState.PENDING, ApiState.FAILED -> presenter?.getOrUpdate(null)
            ApiState.SUCCESS -> Log.i(title, "Network available and data loaded")
        }
    }

}
