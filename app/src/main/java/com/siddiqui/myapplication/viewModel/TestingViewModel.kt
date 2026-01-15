package com.siddiqui.myapplication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class TestingViewModelL: ViewModel() {
    private val _events = MutableSharedFlow<Unit>()
    val events = _events.asSharedFlow()

    fun showToast() = viewModelScope.launch {
        _events.emit(Unit)
    }



}