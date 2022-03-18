package com.dakulangsakalam.customwebview.domain.repository

import com.dakulangsakalam.customwebview.domain.dto.InstallResponseModel
import com.dakulangsakalam.customwebview.domain.dto.JumpResponseModel
import com.dakulangsakalam.customwebview.domain.model.Response
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface JumpRepository {

    suspend fun registerDevice(@Query("androidname") id: String): Flow<Response<InstallResponseModel>>

    suspend fun getJumpUrl(@Query("androidname") id: String): Flow<Response<JumpResponseModel>>
}