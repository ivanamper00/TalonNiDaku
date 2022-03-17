package com.dakulangsakalam.customwebview.domain.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class InstallResponseModel(
    @SerializedName("httpCode") var httpCode: Number,
    @SerializedName("response") var response: List<String>
)
