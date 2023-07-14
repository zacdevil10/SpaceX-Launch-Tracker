package uk.co.zac_h.spacex.feature.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.core.ui.databinding.FragmentVerticalRecyclerviewBinding

@AndroidEntryPoint
class SubscriptionFragment : Fragment(), SubscriptionsCallback {

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private val viewModel: SubscriptionViewModel by viewModels()

    private val subscriptionsAdapter = SubscriptionsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progress.hide()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = subscriptionsAdapter
        }

        subscriptionsAdapter.submitList(viewModel.plans)

        binding.swipeRefresh.setOnRefreshListener {
            subscriptionsAdapter.submitList(viewModel.plans)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun subscribe() {
        viewModel.subscribe()

        subscriptionsAdapter.submitList(viewModel.plans)
    }

    override fun unsubscribe() {
        viewModel.unsubscribe()

        subscriptionsAdapter.submitList(viewModel.plans)
    }
}

interface SubscriptionsCallback {

    fun subscribe()

    fun unsubscribe()
}