package com.dakulangsakalam.customwebview.domain.dto

data class JumpResponseModel(
    val httpCode: Int,
    val response: List<JumpResponse>
)