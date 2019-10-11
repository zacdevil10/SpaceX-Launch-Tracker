package uk.co.zac_h.spacex.about.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_company.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.CompanyModel
import java.text.DecimalFormat

class CompanyFragment : Fragment(), CompanyView {

    private lateinit var presenter: CompanyPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_company, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CompanyPresenterImpl(
            this,
            CompanyInteractorImpl()
        )

        presenter.getCompanyInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequest()
    }

    override fun updateCompanyInfo(companyModel: CompanyModel) {
        with(companyModel.headquarters) {
            company_address_text.text = context?.getString(R.string.address, address, city, state)
        }
        company_summary_text.text = companyModel.summary
        company_founded_text.text =
            context?.getString(R.string.founded, companyModel.founder, companyModel.founded)
        company_ceo_text.text = companyModel.ceo
        company_cto_text.text = companyModel.cto
        company_coo_text.text = companyModel.coo
        company_cto_pro_text.text = companyModel.ctoPropulsion
        company_valuation_text.text =
            DecimalFormat("#,###.00").format(companyModel.valuation).toString()
        company_employees_text.text = companyModel.employees.toString()
        company_vehicles_text.text = companyModel.vehicles.toString()
        company_launch_sites_text.text = companyModel.launchSites.toString()
        company_test_sites_text.text = companyModel.testSites.toString()
    }

    override fun showProgress() {
        company_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        company_progress_bar.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

}