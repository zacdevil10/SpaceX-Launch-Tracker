package uk.co.zac_h.spacex.feature.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import uk.co.zac_h.spacex.feature.settings.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is null" }

    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSettingsBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsAdapter = SettingsAdapter()

        binding.settingsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = settingsAdapter
        }

        settingsAdapter.submitList(
            listOf(
                SettingsHeader("About"),
                SettingsItem(
                    R.drawable.ic_baseline_domain_24,
                    "Company",
                    SettingsFragmentDirections.actionToCompany()
                ),
                SettingsItem(
                    R.drawable.ic_info_outline_black_24dp,
                    "About",
                    SettingsFragmentDirections.actionToAbout()
                ),
                SettingsHeader("Settings"),
                SettingsItem(
                    R.drawable.ic_baseline_brightness_medium_24,
                    "Theme",
                    SettingsFragmentDirections.actionToThemeDialog()
                )
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
