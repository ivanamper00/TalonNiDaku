package com.dakulangsakalam.customwebview.domain.use_case

import com.dakulangsakalam.customwebview.data.repository.JumpRepoImp
import com.dakulangsakalam.customwebview.domain.dto.JumpResponseModel
import com.dakulangsakalam.customwebview.domain.model.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class GetJumpUrl {
    private val repo: JumpRepoImp = JumpRepoImp()

    operator fun invoke(id: String): Flow<Response<JumpResponseModel>> = callbackFlow {
        val response = try {
            Response.Success(repo.getJumpUrl(id))
        }catch (e: Exception){
            Response.Error(e)
        }
        trySend(response)
        awaitClose()
    }
}