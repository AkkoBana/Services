package com.akkobana.services.di

import com.akkobana.services.data.ApiRepository
import com.akkobana.services.data.ApiRepositoryImpl
import com.akkobana.services.domain.usecases.UploadFileUseCase
import com.akkobana.services.domain.usecases.UploadFileUseCaseImpl

interface ApiModule {
    val apiRepository: ApiRepository
    val uploadFileUseCase: UploadFileUseCase
}

class ApiModuleImpl : ApiModule {
    override val apiRepository: ApiRepository by lazy { ApiRepositoryImpl() }
    override val uploadFileUseCase: UploadFileUseCase by lazy { UploadFileUseCaseImpl(apiRepository) }
}