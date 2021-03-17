package uk.co.zac_h.spacex.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uk.co.zac_h.spacex.BuildConfig
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentAboutBinding

class AboutFragment : BaseFragment() {

    override val title: String by lazy { requireContext().getString(R.string.menu_about) }

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAboutBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbarLayout.toolbar.setup()
            toolbarLayout.progress.hide()

            aboutVersion.text =
                context?.getString(R.string.version_name, BuildConfig.VERSION_NAME)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
