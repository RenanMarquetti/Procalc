package com.example.procalc.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.procalc.R
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        val navControler = findNavController(R.id.nav_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_sum_calculations_game, R.id.nav_sub_calculations_game
                , R.id.nav_mult_calculations_game, R.id.nav_div_calculations_game
            ),drawerLayout
        )

        setupActionBarWithNavController(navControler,appBarConfiguration)
        navView.setupWithNavController(navControler)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navControler = findNavController(R.id.nav_fragment_content_main)
        return navControler.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
