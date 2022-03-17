package com.dakulangsakalam.customwebview.data.repository

import com.dakulangsakalam.customwebview.presentation.ui.jump.JumpActivity
import com.dakulangsakalam.customwebview.domain.dto.InstallResponseModel
import com.dakulangsakalam.customwebview.domain.dto.JumpResponseModel
import com.dakulangsakalam.customwebview.domain.repository.JumpRepo
import com.dakulangsakalam.customwebview.presentation.helper.RetrofitHelper
import com.dakulangsakalam.customwebview.presentation.utils.writeLogs
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class JumpRepoImp : JumpRepo {

    init {
        writeLogs("Base URL : ${JumpActivity.getBaseURL(JumpActivity.jumpRequestTrial)}")
    }

    override suspend fun registerDevice(id: String): InstallResponseModel {
        return RetrofitHelper.retrofitService(JumpActivity.getBaseURL()).registerDevice(id)
    }

    override suspend fun getJumpUrl(id: String): JumpResponseModel {
        return RetrofitHelper.retrofitService(JumpActivity.getBaseURL()).getJumpUrl(id)
    }
}