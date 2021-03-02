package com.example.danshal.ui.admin

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.danshal.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class AdminActivity : AppCompatActivity() {

//    lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AdminFragment.newInstance())
                .commitNow()
        }

//        toolbar = !!findViewById(R.id.nav_view)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
//                toolbar.title = "Dashboard"
                val dashboardFragment = AdminFragment.newInstance()
                openFragment(dashboardFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add -> {
//                toolbar.title = "Toeveogen"
                val addFragment = AdminAddFragment.newInstance()
                openFragment(addFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_users -> {
//                toolbar.title = "Gebruikers"
                val usersFragment = AdminUsersFragment.newInstance()
                openFragment(usersFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}