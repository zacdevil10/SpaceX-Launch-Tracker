package uk.co.zac_h.spacex.crew.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.crew.CrewViewModel
import uk.co.zac_h.spacex.crew.adapters.CrewPagerAdapter
import uk.co.zac_h.spacex.databinding.FragmentCrewDetailsBinding
import uk.co.zac_h.spacex.utils.views.DepthPageTransformer

class CrewPagerFragment : BaseFragment() {

    override var title: String = ""

    private val viewModel: CrewViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentCrewDetailsBinding

    private var crew: List<Fragment> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCrewDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setup()

        val crewPagerAdapter = CrewPagerAdapter(childFragmentManager, crew)

        binding.crewPager.apply {
            adapter = crewPagerAdapter
            //setCurrentItem(MainActivity.currentPosition, false)
            setPageTransformer(true, DepthPageTransformer())

            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(p: Int, pOffset: Float, pOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    activity?.title = (crew[position] as BaseFragment).title
                    //MainActivity.currentPosition = position
                }
            })
        }

        viewModel.crew.observe(viewLifecycleOwner) { result ->
            result.data?.let { crewList ->
                //crew = crewList.map { CrewItemFragment.newInstance(it) }
                crewPagerAdapter.notifyDataSetChanged()
            }
        }

        prepareSharedElementTransition()

        if (savedInstanceState == null) postponeEnterTransition()
    }

    private fun prepareSharedElementTransition() {
        sharedElementEnterTransition = MaterialContainerTransform()

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                /*val currentFragment = crew[MainActivity.currentPosition]
                val view = currentFragment.view
                view?.let {
                    names?.get(0)?.let { name ->
                        sharedElements?.put(name, view.findViewById(R.id.item_crew_constraint))
                    }
                } ?: return*/
            }
        })
    }
}