package uk.co.zac_h.spacex.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.AboutWearFragment

import uk.co.zac_h.spacex.base.adapters.NavigationWearAdapter
import uk.co.zac_h.spacex.dashboard.DashboardWearFragment
import uk.co.zac_h.spacex.launches.LaunchesWearFragment

class MainWearActivity : FragmentActivity(), MainWearView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.content_frame, DashboardWearFragment()).commit()

        top_navigation_drawer.apply {
            setAdapter(NavigationWearAdapter(this@MainWearActivity))
            controller.peekDrawer()
            addOnItemSelectedListener {
                when (it) {
                    0 -> replaceFragment(DashboardWearFragment())
                    1 -> replaceFragment(LaunchesWearFragment.newInstance("upcoming"))
                    2 -> replaceFragment(LaunchesWearFragment.newInstance("past"))
                    3 -> replaceFragment(AboutWearFragment())
                }
            }
        }
    }

    override fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit()
    }
}
