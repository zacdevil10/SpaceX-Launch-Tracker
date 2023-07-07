package uk.co.zac_h.spacex.feature.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.feature.subscription.databinding.FragmentSubscriptionBinding

@AndroidEntryPoint
class SubscriptionFragment : Fragment() {

    private lateinit var binding: FragmentSubscriptionBinding

    private val viewModel: SubscriptionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSubscriptionBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.subscribeButton.text = if (viewModel.hasSubscribed) "Unsubscribe" else "Subscribe"

        binding.subscribeButton.setOnClickListener {
            if (viewModel.hasSubscribed) {
                viewModel.unsubscribe()
                binding.subscribeButton.text = "Subscribe"
            } else {
                viewModel.subscribe()
                binding.subscribeButton.text = "Unsubscribe"
            }
        }
    }
}