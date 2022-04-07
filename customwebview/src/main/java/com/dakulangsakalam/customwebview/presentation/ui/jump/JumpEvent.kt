package com.dakulangsakalam.customwebview.presentation.ui.jump

import com.dakulangsakalam.customwebview.domain.dto.Response

sealed class JumpEvent {
    data class Loading(var isLoading: Boolean): JumpEvent()
    data class AppInstalledEvent(var isInstalled: Boolean): JumpEvent()
    data class JumpRequestSuccess(var list: Response): JumpEvent()
    data class JumpRequestError(var exception: Exception): JumpEvent()
}
