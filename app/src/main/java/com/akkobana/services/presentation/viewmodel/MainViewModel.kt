package com.akkobana.services.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akkobana.services.domain.usecases.UploadFileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val uploadFileUseCase: UploadFileUseCase
): ViewModel() {

    val uploadFileProgressLive = MutableLiveData<Int>()

    fun startUploadFile() {
        viewModelScope.launch(Dispatchers.IO) {
            uploadFileUseCase.uploadFile().collect {
                try {
                    uploadFileProgressLive.postValue(it.uploadProgress)
                } catch (error: Exception) {
                    Log.e("MyError", "coroutinesUpload", error)
                }
            }
        }
    }
}