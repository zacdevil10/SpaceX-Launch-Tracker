package uk.co.zac_h.spacex.base

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.adapters.NavigationAdapter
import uk.co.zac_h.spacex.dashboard.DashboardWearFragment

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        fragmentManager.beginTransaction().replace(R.id.content_frame, DashboardWearFragment()).commit()

        top_navigation_drawer.apply {
            setAdapter(NavigationAdapter(this@MainActivity))
            controller.peekDrawer()
            addOnItemSelectedListener {
                when (it) {
                    0 -> fragmentManager.beginTransaction().replace(R.id.content_frame, DashboardWearFragment()).commit()
                }
            }
        }
    }
}
