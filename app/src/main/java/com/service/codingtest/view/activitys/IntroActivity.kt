package com.service.codingtest.view.activitys

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.service.codingtest.R
import com.service.codingtest.network.Constant


class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startMainActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(this@IntroActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}