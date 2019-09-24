package uk.co.zac_h.spacex.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.zac_h.spacex.R

import uk.co.zac_h.spacex.base.adapters.NavigationWearAdapter
import uk.co.zac_h.spacex.dashboard.DashboardWearFragment
import uk.co.zac_h.spacex.utils.WIPWearFragment

class MainWearActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.content_frame, DashboardWearFragment()).commit()

        top_navigation_drawer.apply {
            setAdapter(NavigationWearAdapter(this@MainWearActivity))
            controller.peekDrawer()
            addOnItemSelectedListener {
                when (it) {
                    0 -> supportFragmentManager.beginTransaction().replace(R.id.content_frame, DashboardWearFragment()).commit()
                    1 -> showWIPFragment()
                    2 -> showWIPFragment()
                    3 -> showWIPFragment()
                }
            }
        }
    }

    private fun showWIPFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.content_frame, WIPWearFragment()).commit()
    }
}