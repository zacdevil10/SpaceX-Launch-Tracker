package uk.co.zac_h.spacex.about.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentCompanyBinding
import uk.co.zac_h.spacex.dto.spacex.Company
import uk.co.zac_h.spacex.utils.openWebLink
import uk.co.zac_h.spacex.utils.orUnknown

@AndroidEntryPoint
class CompanyFragment : BaseFragment() {

    override val title: String by lazy { getString(R.string.menu_company) }

    private val viewModel: CompanyViewModel by viewModels()

    private lateinit var binding: FragmentCompanyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCompanyBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout.toolbar.setup()

        viewModel.company.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                ApiResult.Status.PENDING -> showProgress()
                ApiResult.Status.SUCCESS -> result.data?.let { update(it) }
                ApiResult.Status.FAILURE -> showError(result.error?.message.orUnknown())
            }
        }

        viewModel.getCompany()
    }

    fun update(response: Company) {
        with(binding) {
            hideProgress()
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

    fun showProgress() {
        binding.toolbarLayout.progress.show()
    }

    fun hideProgress() {
        binding.toolbarLayout.progress.hide()
    }

    fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.getCompany()
    }

}
