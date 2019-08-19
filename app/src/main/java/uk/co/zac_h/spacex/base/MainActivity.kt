package uk.co.zac_h.spacex.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import uk.co.zac_h.spacex.R

class MainActivity : AppCompatActivity() {

    private val startDestinations =
        mutableSetOf(R.id.dashboard_page_fragment, R.id.launches_page_fragment, R.id.statistics_page_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig = AppBarConfiguration.Builder(startDestinations).setDrawerLayout(drawerLayout).build()

        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, appBarConfig)
        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)

    }

}
