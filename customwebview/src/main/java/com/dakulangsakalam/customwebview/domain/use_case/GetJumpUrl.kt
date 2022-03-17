package com.dakulangsakalam.customwebview.domain.use_case

import com.dakulangsakalam.customwebview.data.repository.JumpRepoImp
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class GetJumpUrl {
    private val repo = JumpRepoImp.getInstance()

    suspend operator fun invoke(id: String) = repo.getJumpUrl(id)
}