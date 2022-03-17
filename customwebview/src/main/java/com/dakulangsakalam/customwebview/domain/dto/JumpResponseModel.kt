package com.dakulangsakalam.customwebview.domain.dto

import androidx.annotation.Keep

@Keep
data class JumpResponseModel(
    val httpCode: Int,
    val response: List<JumpResponse>
)