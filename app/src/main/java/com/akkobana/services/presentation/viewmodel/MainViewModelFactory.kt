package com.akkobana.services.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akkobana.services.presentation.ui.MainApplication

class MainViewModelFactory: ViewModelProvider.Factory {

    private val uploadFileUseCase by lazy { MainApplication.apiModule.uploadFileUseCase }

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MainViewModel(uploadFileUseCase) as T

}