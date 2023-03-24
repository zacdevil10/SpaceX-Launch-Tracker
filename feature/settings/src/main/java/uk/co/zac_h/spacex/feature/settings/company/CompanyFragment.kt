package uk.co.zac_h.spacex.feature.settings.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.fragment.openWebLink
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.feature.settings.databinding.FragmentCompanyBinding
import uk.co.zac_h.spacex.network.ApiResult

class CompanyFragment : BaseFragment() {

    private val viewModel: CompanyViewModel by viewModels()

    private lateinit var binding: FragmentCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCompanyBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.company.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Pending -> showProgress()
                is ApiResult.Success -> result.data?.let { update(it) }
                is ApiResult.Failure -> showError(result.exception.message.orUnknown())
            }
        }

        viewModel.getCompany()
    }

    fun update(response: Company) {
        with(binding) {
            hideProgress()

            companyWebsite.isVisible = response.website != null
            response.website?.let { website ->
                companyWebsite.setOnClickListener { openWebLink(website) }
            }

            companyWiki.isVisible = response.wiki != null
            response.wiki?.let { wiki ->
                companyWiki.setOnClickListener { openWebLink(wiki) }
            }

            companySummary.text = response.description
            companyFounded.text = response.foundingYear
            companyCeo.text = response.administrator?.removePrefix("CEO: ")
        }
    }

    private fun showProgress() {
        binding.content.visibility = View.INVISIBLE
        binding.progress.show()
    }

    private fun hideProgress() {
        binding.content.visibility = View.VISIBLE
        binding.progress.hide()
    }

    fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.getCompany()
    }
}
