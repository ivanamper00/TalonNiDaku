package com.dakulangsakalam.customwebview.domain.model

data class JumpRequest(
    val androidname: String,
    val apistatus: String,
    val domainswitch: Int,
    val retryDomain: Int
)