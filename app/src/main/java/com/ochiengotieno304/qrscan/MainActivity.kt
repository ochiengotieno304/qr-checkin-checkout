package com.ochiengotieno304.qrscan


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private var checkInFragment = CheckInFragment()
    private var checkOutFragment = CheckOutFragment()
    private var historyFragment = HistoryFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val headerView : View = navigationView.getHeaderView(0)
        val navUsername: TextView = headerView.findViewById(R.id.text_view_username)
        val sharedPreferences: SharedPreferences =
            applicationContext.getSharedPreferences("LoggedInUserPrefs", Context.MODE_PRIVATE)

        val username = sharedPreferences.getString("username", "")
        navUsername.text = username

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, checkInFragment).commit()
            navigationView.setCheckedItem(R.id.nav_check_in)
        }
    }

    @Override
    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_check_in -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<CheckInFragment>(R.id.fragment_container)
                }
            }
            R.id.nav_check_out -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, checkOutFragment).commit()
            }
            R.id.history -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, historyFragment).commit()
            }
            R.id.logout -> {
                val sharedPreferences: SharedPreferences =
                    applicationContext.getSharedPreferences("LoggedInUserPrefs", Context.MODE_PRIVATE)
                applicationContext.deleteSharedPreferences("LoggedInUSerPrefs")

                sharedPreferences.edit().remove("username").apply()
                sharedPreferences.edit().remove("password").apply()
                // redirect to login page
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
