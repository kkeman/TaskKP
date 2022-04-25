package com.service.codingtest.view.activitys

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.service.codingtest.R
import com.service.codingtest.view.fragments.FavoriteFragment
import com.service.codingtest.view.fragments.ImageFragment


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val imageFragment: Fragment = ImageFragment()
        val favoriteFragment: Fragment = FavoriteFragment()
        val fm: FragmentManager = supportFragmentManager
        var active: Fragment = imageFragment

        fm.beginTransaction().add(R.id.main_container, favoriteFragment, "2").hide(favoriteFragment)
            .commit();
        fm.beginTransaction().add(R.id.main_container, imageFragment, "1").commit();

        val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
            object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                    when (item.getItemId()) {
                        R.id.navigation_image -> {
                            fm.beginTransaction().hide(active).show(imageFragment).commit()
                            active = imageFragment
                            return true
                        }
                        R.id.navigation_favorite -> {
                            fm.beginTransaction().hide(active).show(favoriteFragment).commit()
                            active = favoriteFragment
                            return true
                        }
                    }
                    return false
                }
            }
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
