package com.akkobana.services.domain.usecases

import com.akkobana.services.data.ApiRepository
import com.akkobana.services.domain.entities.FileEntity
import kotlinx.coroutines.flow.Flow

class UploadFileUseCaseImpl (
    private val apiRepository: ApiRepository
) : UploadFileUseCase {
    override suspend fun uploadFile(): Flow<FileEntity> =
        apiRepository.uploadFile()
}