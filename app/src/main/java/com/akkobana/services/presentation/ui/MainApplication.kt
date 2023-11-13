package com.akkobana.services.presentation.ui

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.akkobana.services.di.ApiModule
import com.akkobana.services.di.ApiModuleImpl
import com.akkobana.services.presentation.service.ForegroundService


class MainApplication: Application() {

    companion object {
        lateinit var apiModule: ApiModule
    }

    override fun onCreate() {
        super.onCreate()
        apiModule = ApiModuleImpl()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ForegroundService.CHANNEL_ID,
                "Running Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }
}