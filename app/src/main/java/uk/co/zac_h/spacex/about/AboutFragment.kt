package uk.co.zac_h.spacex.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uk.co.zac_h.spacex.BuildConfig
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentAboutBinding
import uk.co.zac_h.spacex.databinding.ToolbarProgressBinding

class AboutFragment : BaseFragment() {

    override val title: String by lazy { getString(R.string.menu_about) }

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAboutBinding.inflate(inflater, container, false).apply {
        binding = this
        toolbarBinding = ToolbarProgressBinding.bind(binding.root)
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbarBinding.toolbar.setup()
            toolbarBinding.progress.hide()

            aboutVersion.text = getString(R.string.version_name, BuildConfig.VERSION_NAME)
        }
    }
}
