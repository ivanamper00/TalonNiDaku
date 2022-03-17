package com.dakulangsakalam.customwebview.domain.dto

import com.google.gson.annotations.SerializedName

data class InstallResponseModel(
    @SerializedName("httpCode") var httpCode: Number,
    @SerializedName("response") var response: List<String>
)
