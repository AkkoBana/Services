package com.akkobana.services.presentation.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.akkobana.services.R
import com.akkobana.services.domain.usecases.UploadFileUseCase
import com.akkobana.services.presentation.ui.MainActivity
import com.akkobana.services.presentation.ui.MainApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForegroundService : Service() {

    private lateinit var uploadFileUseCase: UploadFileUseCase

    override fun onCreate() {
        super.onCreate()
        uploadFileUseCase = MainApplication.apiModule.uploadFileUseCase
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Action.START.toString() -> start()
            Action.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Notification")
            .setContentText("")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        CoroutineScope(Dispatchers.IO).launch {
            uploadFileUseCase.uploadFile().collect {
                if (it.uploadProgress != MainActivity.COMPLETE) {
                    notificationManager?.notify(
                        1,
                        notification.setContentText(it.uploadProgress.toString() + "%").build()
                    )
                } else {
                    stopSelf()
                }
                println(it.uploadProgress)
            }
        }
        startForeground(1, notification.build())
    }

    enum class Action {
        START, STOP
    }

    companion object {
        const val CHANNEL_ID = "running_channel"
    }
}