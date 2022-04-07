package com.dakulangsakalam.customwebview.data.repository

import com.dakulangsakalam.customwebview.presentation.ui.jump.JumpActivity
import com.dakulangsakalam.customwebview.domain.model.JumpRequest
import com.dakulangsakalam.customwebview.domain.repository.JumpRepository
import com.dakulangsakalam.customwebview.presentation.helper.RetrofitHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class JumpRepoImp : JumpRepository {

    val service = RetrofitHelper.retrofitService(JumpActivity.getBaseURL())

    override suspend fun startRequest(param: JumpRequest) = service.startRequest(param)

}