package com.siddiqui.myapplication.viewModel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.siddiqui.myapplication.model.AudioRecorder

class RecordingViewModel(context: Context): ViewModel() {
    private val _recordings = mutableStateListOf<String>()
    val recordings:List<String> get() = _recordings

    private var recorder: AudioRecorder?= null
    private var isRecording = false

    init {
        recorder = AudioRecorder(context)
    }

    fun startRecoding(){
        if (!isRecording){
            recorder?.startRecording()
            isRecording = true
        }
    }

    fun stopRecording(){
        if (isRecording){
            val file = recorder?.stopRecording()
            isRecording = false
            file?.let { _recordings.add(it) } // save to list
        }
    }

    fun isRecording() = isRecording


}