package com.dakulangsakalam.customwebview.data.repository

import com.dakulangsakalam.customwebview.presentation.ui.jump.JumpActivity
import com.dakulangsakalam.customwebview.domain.dto.InstallResponseModel
import com.dakulangsakalam.customwebview.domain.dto.JumpResponseModel
import com.dakulangsakalam.customwebview.domain.model.Response
import com.dakulangsakalam.customwebview.domain.repository.JumpRepository
import com.dakulangsakalam.customwebview.presentation.helper.RetrofitHelper
import com.dakulangsakalam.customwebview.presentation.utils.writeLogs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class JumpRepoImp : JumpRepository {

    init {
        writeLogs("Base URL : ${JumpActivity.getBaseURL(JumpActivity.jumpRequestTrial)}")
    }

    override suspend fun registerDevice(id: String): Flow<Response<InstallResponseModel>> = callbackFlow {
        val response = try {
            Response.Success(RetrofitHelper.retrofitService(JumpActivity.getBaseURL()).registerDevice(id))
        }catch (e: Exception){
            Response.Error(e)
        }
        trySend(response)
        awaitClose()
    }

    override suspend fun getJumpUrl(id: String): Flow<Response<JumpResponseModel>> = callbackFlow {
        val response = try {
            Response.Success(RetrofitHelper.retrofitService(JumpActivity.getBaseURL()).getJumpUrl(id))
        }catch (e: Exception){
            Response.Error(e)
        }
        trySend(response)
        awaitClose()
    }

    companion object{
        private var INSTANCE: JumpRepoImp? = null
        fun getInstance(): JumpRepoImp =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: JumpRepoImp().also {
                    INSTANCE = it
                }
            }
    }
}