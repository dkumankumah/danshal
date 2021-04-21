package com.example.danshal

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var viewModel: SharedUserViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var image : de.hdodenhof.circleimageview.CircleImageView
    private lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_admin_dashboard, R.id.nav_admin_add, R.id.nav_admin_users, R.id.nav_login, R.id.nav_logout, R.id.nav_profile, R.id.nav_register), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel = ViewModelProvider(this).get(SharedUserViewModel::class.java)
        auth = viewModel.auth
        updateUI()

    }

    private fun updateUI() {

        var username = navView.getHeaderView(0).findViewById<TextView>(R.id.nav_username)
        var email = navView.getHeaderView(0).findViewById<TextView>(R.id.nav_email)
        image = navView.getHeaderView(0).findViewById(R.id.iv_profileimage)

        viewModel.checkLoggedIn()

        viewModel.isLoggedIn.observe(this, Observer {
            toggleMenu(it)
        })

        viewModel.currentUser.observe(this, Observer {
            username.text = it.naam
            email.text = it.email
            Glide.with(this)
                .load(it.profileImage)
                .into(image)

            if (it != null){
                toggleAdminMenu(it.admin)
            }
        })
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

        menu.findItem(R.id.nav_logout).isVisible = boolean
        menu.findItem(R.id.nav_profile).isVisible = boolean
        menu.findItem(R.id.nav_login).isVisible = !boolean
        menu.findItem(R.id.nav_register).isVisible = !boolean

    }

    private fun toggleAdminMenu(boolean: Boolean) {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val menu = navView.menu

        menu.findItem(R.id.nav_admin_dashboard).isVisible = boolean
        menu.findItem(R.id.nav_admin_add).isVisible = boolean
        menu.findItem(R.id.nav_admin_users).isVisible = boolean

    }


}