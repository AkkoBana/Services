package com.akkobana.services.data

import com.akkobana.services.domain.entities.FileEntity
import kotlinx.coroutines.flow.Flow

interface ApiRepository {
    suspend fun uploadFile(): Flow<FileEntity>
}