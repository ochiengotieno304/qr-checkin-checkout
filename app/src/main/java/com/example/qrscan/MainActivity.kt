package com.example.qrscan


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var drawer: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var navigationView: NavigationView
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

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, checkInFragment).commit()
            navigationView.setCheckedItem(R.id.nav_check_in)
        }


//        val qrButton: Button = findViewById(R.id.qr_button)
//        qrButton.setOnClickListener {
//            val intentIntegrator = IntentIntegrator(this)
//            setDesiredBarcodeFormats()
//            intentIntegrator.initiateScan()
//        }
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
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, checkInFragment).commit()
            }
            R.id.nav_check_out -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, checkOutFragment).commit()
            }
            R.id.history -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, historyFragment).commit()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

//    @Deprecated("Deprecated in Java")
//    @Suppress("DEPRECATION")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        val result = IntentIntegrator.parseActivityResult(resultCode, data)
//        if (result != null) {
//            MaterialAlertDialogBuilder(this)
//                .setMessage("Would you like to go to ${result.contents}?")
//                .setPositiveButton("Yes") { _, _ ->
//                    val intent = Intent(Intent.ACTION_WEB_SEARCH)
//                    intent.putExtra(SearchManager.QUERY, result.contents)
//                    startActivity(intent)
//                }
//                .setNegativeButton("No") { _, _ -> }
//                .create()
//                .show()
//        }
//    }
}

//private fun setDesiredBarcodeFormats() {
//
//}
