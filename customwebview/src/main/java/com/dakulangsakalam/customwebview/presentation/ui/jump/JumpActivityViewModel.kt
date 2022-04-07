package com.dakulangsakalam.customwebview.presentation.ui.jump

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dakulangsakalam.customwebview.domain.model.JumpRequest
import com.dakulangsakalam.customwebview.domain.model.Response
import com.dakulangsakalam.customwebview.domain.use_case.GetJumpUrl
import com.dakulangsakalam.customwebview.domain.use_case.RegisterDevice
import com.dakulangsakalam.customwebview.presentation.utils.writeLogs
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class JumpActivityViewModel : ViewModel() {

    private val getJumpUrl = GetJumpUrl()
    private val registerDevice = RegisterDevice()

    private var _jumpEvent = MutableLiveData<JumpEvent>()
    val jumpEvent: LiveData<JumpEvent> get() = _jumpEvent

    fun startRequest(id: String, domainSwitch: Int) {
        val param = JumpRequest(id, "install", domainSwitch)

        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _jumpEvent.postValue(JumpEvent.JumpRequestError(throwable as Exception))
        }){
            val data = registerDevice(param).body()
            _jumpEvent.value = JumpEvent.AppInstalledEvent(data?.httpCode == 200)
        }
    }

    fun getApplicationUrl(id: String, domainSwitch: Int) {
        val param = JumpRequest(id, "androidAPI", domainSwitch)
       viewModelScope.launch (CoroutineExceptionHandler { _, throwable ->
           _jumpEvent.postValue(JumpEvent.JumpRequestError(throwable as Exception))
       }) {
           try{
               val data = getJumpUrl(param).body()
               _jumpEvent.value =
                   JumpEvent.JumpRequestSuccess(data?.response?.get(0)!!)
           }catch (e: Exception){
               _jumpEvent.value = JumpEvent.JumpRequestError(e)
           }
       }
    }
}