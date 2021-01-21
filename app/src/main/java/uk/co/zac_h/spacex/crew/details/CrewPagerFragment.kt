package uk.co.zac_h.spacex.crew.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.crew.adapters.CrewPagerAdapter
import uk.co.zac_h.spacex.databinding.FragmentCrewDetailsBinding
import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.utils.views.DepthPageTransformer

class CrewPagerFragment : Fragment() {

    private var binding: FragmentCrewDetailsBinding? = null

    private lateinit var crewPagerAdapter: CrewPagerAdapter
    private lateinit var crewArray: ArrayList<Crew>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crewArray = when {
            savedInstanceState != null -> {
                savedInstanceState.getParcelableArrayList("crew") ?: ArrayList()
            }
            arguments != null -> {
                requireArguments().getParcelableArrayList<Crew>("crew") as ArrayList<Crew>
            }
            else -> {
                ArrayList()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCrewDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        crewPagerAdapter = CrewPagerAdapter(childFragmentManager, crewArray)

        binding?.crewPager?.apply {
            adapter = crewPagerAdapter
            currentItem = MainActivity.currentPosition
            setPageTransformer(
                true,
                DepthPageTransformer()
            )

            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(p: Int, pOffset: Float, pOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    activity?.title = crewArray[position].name
                    MainActivity.currentPosition = position
                }
            })
        }

        prepareSharedElementTransition()

        if (savedInstanceState == null) postponeEnterTransition()
    }

    private fun prepareSharedElementTransition() {
        sharedElementEnterTransition = MaterialContainerTransform()

        binding?.apply {
            setEnterSharedElementCallback(object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: MutableList<String>?,
                    sharedElements: MutableMap<String, View>?
                ) {
                    val currentFragment = crewPager.adapter?.instantiateItem(
                        crewPager,
                        MainActivity.currentPosition
                    ) as Fragment
                    val view = currentFragment.view
                    view?.let {
                        names?.get(0)?.let { name ->
                            sharedElements?.put(name, view.findViewById(R.id.item_crew_constraint))
                        }
                    } ?: return
                }
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("crew", crewArray)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}