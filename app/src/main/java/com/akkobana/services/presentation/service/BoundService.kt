package com.akkobana.services.presentation.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.akkobana.services.domain.usecases.UploadFileUseCase
import com.akkobana.services.presentation.ui.MainApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BoundService: Service() {

    private lateinit var uploadFileUseCase: UploadFileUseCase
    private var onProgress: ((Int) -> Unit)? = null

    override fun onBind(intent: Intent?): IBinder = UploadBinder()

    inner class UploadBinder: Binder() {
        fun subscribeToProgress(onProgress: (Int) -> Unit) {
            this@BoundService.onProgress = onProgress
        }
    }

    override fun onCreate() {
        super.onCreate()
        uploadFileUseCase = MainApplication.apiModule.uploadFileUseCase
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Action.START.toString() -> startUpload()
            Action.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startUpload() {
        CoroutineScope(Dispatchers.IO).launch {
            uploadFileUseCase.uploadFile().collect {

                println(it.uploadProgress)
                onProgress?.invoke(it.uploadProgress)
            }
        }
    }

    enum class Action {
        START, STOP
    }
}