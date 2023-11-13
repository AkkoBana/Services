package com.akkobana.services.domain.usecases

import com.akkobana.services.domain.entities.FileEntity
import kotlinx.coroutines.flow.Flow

interface UploadFileUseCase {
    suspend fun uploadFile(): Flow<FileEntity>
}