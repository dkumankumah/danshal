package com.example.danshal

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var viewModel: SharedUserViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var image : de.hdodenhof.circleimageview.CircleImageView
    private lateinit var navView: NavigationView

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var menu: Menu


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        menu = navView.menu

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_admin_dashboard, R.id.nav_admin_add, R.id.nav_admin_users, R.id.nav_login, R.id.nav_profile, R.id.nav_register), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel = ViewModelProvider(this).get(SharedUserViewModel::class.java)
        auth = viewModel.auth
        navView.setNavigationItemSelectedListener(this)

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
        menu.findItem(R.id.nav_logout).isVisible = boolean
        menu.findItem(R.id.nav_profile).isVisible = boolean
        menu.findItem(R.id.nav_login).isVisible = !boolean
        menu.findItem(R.id.nav_register).isVisible = !boolean
    }

    private fun toggleAdminMenu(boolean: Boolean) {
        menu.findItem(R.id.nav_admin_dashboard).isVisible = boolean
        menu.findItem(R.id.nav_admin_add).isVisible = boolean
        menu.findItem(R.id.nav_admin_users).isVisible = boolean
    }

    private fun signOutAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.authtenticatie))
        builder.setMessage(R.string.waarschuwing_uitloggen)
        builder.setPositiveButton(R.string.msg_ja) { dialogInterface, _ ->
            auth.signOut()
            viewModel.checkLoggedIn()
            findNavController(R.id.nav_host_fragment).popBackStack(R.id.nav_home, false)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(R.string.msg_nee) { dialog, id ->
            dialog.dismiss()
        }
        builder.show()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_logout -> {
                signOutAlert()
            }
        }
        val mDrawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        mDrawerLayout.closeDrawer(Gravity.LEFT, true)

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.onNavDestinationSelected(item, navController)

        return true
    }



}