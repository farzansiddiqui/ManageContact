package com.siddiqui.myapplication.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun TestingScreen(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FilledTonalButton(onClick = {}) {
            Text(text = "Click")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TestingScreenPreview(){
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(text = "first screen")
        }
        items(5){
            Text(text = "item $it")
        }
        item {
            Text(text = "second screen")
        }
    }

}


sealed class MainIntent {
    object LoadData : MainIntent()
    data class SubmitForm(val input: String) : MainIntent()
}

data class MainState(
    val isLoading: Boolean = false,
    val data:List<String>? = null,
    val error:String?=null
)

class MainViewModel: ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    fun handleIntent(intent: MainIntent){
        when(intent){
           is MainIntent.LoadData -> loadData()
            is MainIntent.SubmitForm -> submitForm(intent.input)
        }
    }

    private fun loadData(){
        viewModelScope.launch {
            _state.value  = MainState(isLoading = false)
            try {
                val data = loadData()
                _state.value = MainState(data = listOf())
            }catch (e: Exception){
                _state.value = MainState(error = e.message)
            }
        }
    }
    private fun submitForm(input: String){
        viewModelScope.launch {
            _state.value = MainState(isLoading = true)
            try {
                val result = submitForm(input)
                _state.value = MainState(data = listOf())
            }catch (e: Exception){
                _state.value = MainState(error = e.message)
            }
        }
    }
}