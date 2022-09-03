package com.example.sklad.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
     val skladRepo by lazy { SkladRepository.get() }

    private var _result: MutableLiveData<Boolean> = MutableLiveData()
    val result: LiveData<Boolean> get() = _result

    fun registerUser(name: String, password: String) {
        viewModelScope.launch {
            _result.value = skladRepo.registerNewUser(name, password)
        }
    }
}