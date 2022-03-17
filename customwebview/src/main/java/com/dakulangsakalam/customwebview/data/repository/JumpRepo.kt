package com.dakulangsakalam.customwebview.data.repository

import com.dakulangsakalam.customwebview.domain.dto.InstallResponseModel
import com.dakulangsakalam.customwebview.domain.dto.JumpResponseModel
import retrofit2.http.POST
import retrofit2.http.Query

interface JumpRepo {

    @POST("install")
    suspend fun registerDevice(@Query("androidname") id: String): InstallResponseModel

    @POST("androidAPI")
    suspend fun getJumpUrl(@Query("androidname") id: String): JumpResponseModel
}