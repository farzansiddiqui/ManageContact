package com.siddiqui.myapplication.model

import com.siddiqui.myapplication.R

data class UserProfile(val name: String, val status: Boolean, val drawableInt: Int)

val userProfileListItem = arrayListOf(
    UserProfile(name = "John Doe", status = true, R.drawable.profile_picture),
    UserProfile(name = "Farzan", status = false, R.drawable.profile_photo),
    UserProfile(name = "Jolley", status = true, R.drawable.profile_photo),
    UserProfile(name = "AllenJolly", status = false, R.drawable.profile_picture2),
)