package com.akkobana.services.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import com.akkobana.services.presentation.service.ForegroundService

fun Intent.startService(context: Context, service: Class<*>) {
    this.action = Action.START.toString()
    if (service == ForegroundService::class.java) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(this)
        } else {
            context.startService(this)
        }
    } else {
        context.startService(this)
    }

}


enum class Action {
    START, STOP
}
