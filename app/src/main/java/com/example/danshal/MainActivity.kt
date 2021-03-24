package com.example.danshal

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        auth = Firebase.auth
        var currentuser = auth.currentUser


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_admin_dashboard, R.id.nav_admin_add, R.id.nav_admin_users, R.id.nav_login, R.id.nav_logout, R.id.nav_profile, R.id.nav_register), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
//            item ->
//                when(item.itemId) {
//                    R.id.nav_logout -> {
//                        auth.signOut()
//                    }
//                }
//            true
//        })



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        auth = Firebase.auth

        var currentuser = auth.currentUser

        if (currentuser != null){
            Log.d("Mainapplication", "currentuser is logged in")
            toggleMenu(true)

        }else {
            Log.d("Mainapplication", "currentuser is logged out")
            toggleMenu(false)
        }

    }

    private fun toggleMenu(boolean: Boolean) {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val menu = navView.menu
        if(boolean) {
            menu.findItem(R.id.nav_logout).isVisible = true
            menu.findItem(R.id.nav_profile).isVisible = true
            menu.findItem(R.id.nav_login).isVisible = false
            menu.findItem(R.id.nav_register).isVisible = false
        } else {
            menu.findItem(R.id.nav_logout).isVisible = false
            menu.findItem(R.id.nav_profile).isVisible = false
            menu.findItem(R.id.nav_login).isVisible = true
            menu.findItem(R.id.nav_register).isVisible = true
        }

    }


}