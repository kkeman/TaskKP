package com.service.codingtest.network

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import com.service.codingtest.R
import com.service.codingtest.view.activitys.BaseActivity
import android.os.Environment

object Util {

    fun getNetworkConnect(context: BaseActivity): Boolean {
        var state = false

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null)
            state = true

        return state
    }

    fun defaultPopup(context: Context, msg: Int) {
        Handler(Looper.getMainLooper()).post {
            AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.alert))
                    .setMessage(msg)
                    .setPositiveButton(context.getString(R.string.ok))
                    { dialog, _ ->
                        dialog.dismiss()
                    }.create().show()
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        var state = false

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null)
            state = true

        return state
    }

    fun getPicturesDir(context: Context): String = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.path
}