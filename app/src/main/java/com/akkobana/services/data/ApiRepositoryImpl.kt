package com.akkobana.services.data

import com.akkobana.services.domain.entities.FileEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApiRepositoryImpl : ApiRepository {
    override suspend fun uploadFile(): Flow<FileEntity> =
        flow {
            for (i in 1..100) {
                delay(300)
                emit(FileEntity(i))
            }
        }
}