package com.siddiqui.myapplication.model.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TestingViewModel: ViewModel() {
    val _data = MutableLiveData<String>()
    val data: LiveData<String> = _data



}