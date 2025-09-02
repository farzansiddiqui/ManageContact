package com.siddiqui.myapplication.model

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import java.io.File

class AudioRecorder(private val context: Context) {

    private var mediaRecorder: MediaRecorder? = null
    private var  outputFile:String?=null

    fun startRecording():String?{
        val fileName = "recording_${System.currentTimeMillis()}.mp3"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)
        outputFile = file.absolutePath

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile)
            prepare()
            start()
        }
        return outputFile
    }

    fun stopRecording():String? {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        return outputFile
    }


}