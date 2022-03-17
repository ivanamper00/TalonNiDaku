package com.dakulangsakalam.customwebview.domain.dto

data class JumpResponse(
    val count: Int,
    val firstResult: Int,
    val html: String,
    val list: List<JumpList>,
    val maxResults: Int,
    val pageNo: Int,
    val pageSize: Int
)