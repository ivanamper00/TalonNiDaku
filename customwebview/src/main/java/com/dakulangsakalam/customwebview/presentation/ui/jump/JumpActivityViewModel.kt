package com.dakulangsakalam.customwebview.presentation.ui.jump

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dakulangsakalam.customwebview.domain.model.Response
import com.dakulangsakalam.customwebview.domain.use_case.GetJumpUrl
import com.dakulangsakalam.customwebview.domain.use_case.RegisterDevice
import com.dakulangsakalam.customwebview.presentation.utils.writeLogs
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class JumpActivityViewModel : ViewModel() {

    private val getJumpUrl = GetJumpUrl()
    private val registerDevice = RegisterDevice()

    private var _jumpEvent = MutableLiveData<JumpEvent>()
    val jumpEvent: LiveData<JumpEvent> get() = _jumpEvent

    fun startRequest(id: String) {
        viewModelScope.launch{
            registerDevice(id).collect {
                when(it){
                    is Response.Success -> {
                        writeLogs("Register Device Response: ${Gson().toJson(it.data)}")
                        _jumpEvent.value =
                            JumpEvent.AppInstalledEvent(it.data.httpCode == 200)
                    }
                    is Response.Error -> {
                        writeLogs("registerDevice ${it.exception.message}")
                    }
                }
            }
        }
    }

    fun getApplicationUrl(id: String) {
       viewModelScope.launch {
           getJumpUrl(id).collect {
               when(it){
                   is Response.Success -> {
                       writeLogs("Get Url Response: ${Gson().toJson(it.data)}")
                       try{
                           _jumpEvent.value =
                               JumpEvent.JumpRequestSuccess(it.data.response[0].list[0])
                       }catch (e: Exception){
                           _jumpEvent.value = JumpEvent.JumpRequestError(e)
                       }
                   }
                   is Response.Error -> _jumpEvent.value = JumpEvent.JumpRequestError(it.exception)
               }
           }
       }
    }

}