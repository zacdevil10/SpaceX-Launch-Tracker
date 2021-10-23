package uk.co.zac_h.spacex.about.history.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.databinding.FragmentHistoryFilterBinding
import uk.co.zac_h.spacex.utils.BottomDrawerFragment

@AndroidEntryPoint
class HistoryFilterFragment : BottomDrawerFragment() {

    private val viewModel: HistoryFilterViewModel by activityViewModels()

    private lateinit var binding: FragmentHistoryFilterBinding

    override val container: ConstraintLayout by lazy { binding.container }
    override val scrim: View by lazy { binding.scrim }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHistoryFilterBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.doOnTextChanged { text, _, _, _ ->
            viewModel.searchText.value = text?.toString().orEmpty()
        }
    }

}