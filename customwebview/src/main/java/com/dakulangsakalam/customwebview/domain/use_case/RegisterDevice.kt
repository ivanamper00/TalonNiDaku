package com.dakulangsakalam.customwebview.domain.use_case

import com.dakulangsakalam.customwebview.data.repository.JumpRepoImp
import com.dakulangsakalam.customwebview.domain.dto.InstallResponseModel
import com.dakulangsakalam.customwebview.domain.model.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class RegisterDevice {
    private val repo = JumpRepoImp.getInstance()

    suspend operator fun invoke(id: String) = repo.registerDevice(id)
}