package com.akkobana.services.presentation.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.akkobana.services.databinding.ActivityMainBinding
import com.akkobana.services.presentation.service.BackgroundService
import com.akkobana.services.presentation.service.BoundService
import com.akkobana.services.presentation.service.ForegroundService
import com.akkobana.services.presentation.viewmodel.MainViewModel
import com.akkobana.services.presentation.viewmodel.MainViewModelFactory
import com.akkobana.services.utils.startService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val uploadBinder = (service as BoundService.UploadBinder)
            lifecycleScope.launch(Dispatchers.IO) {
                uploadBinder.subscribeToProgress { progress ->
                    try {
                        if (progress != COMPLETE) binding.textView.text = progress.toString() + "%"
                        else binding.textView.text = "done"
                    } catch (error: Exception) {
                        Log.e("MyError", "coroutinesUpload", error)
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {}

    }

    override fun onStart() {
        super.onStart()
        viewModel =
            ViewModelProvider(this, MainViewModelFactory())[MainViewModel::class.java]

        Intent(applicationContext, BoundService::class.java).also {
            bindService(it, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setupListeners()
        observeValues()
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    private fun setupListeners() = with(binding) {
        backgroundButton.setOnClickListener {
            Intent(applicationContext, BackgroundService::class.java).startService(
                applicationContext,
                BackgroundService::class.java
            )
            textView.text = "Background service started"
        }
        foregroundButton.setOnClickListener {
            Intent(applicationContext, ForegroundService::class.java).startService(
                applicationContext,
                ForegroundService::class.java
            )
            textView.text = "Foreground service started"
        }
        boundButton.setOnClickListener {
            Intent(applicationContext, BoundService::class.java).startService(
                applicationContext,
                BoundService::class.java
            )
            textView.text = "Bound service started"
        }
        startUploadButton.setOnClickListener {
            viewModel.startUploadFile()
        }
    }

    private fun observeValues() {
        viewModel.uploadFileProgressLive.observe(this) {
            if (it != COMPLETE) {
                binding.textView.text = it.toString() + "%"
            } else {
                binding.textView.text = "upload done"
            }
        }
    }

    companion object {
        const val COMPLETE = 100
    }
}