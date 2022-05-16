package com.service.codingtest.view.activitys

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.service.codingtest.R
import com.service.codingtest.view.fragments.ImageFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        val imageFragment: Fragment = ImageFragment()
        val fm: FragmentManager = supportFragmentManager

        fm.beginTransaction().add(R.id.main_container, imageFragment, "1").commit()
    }
}
