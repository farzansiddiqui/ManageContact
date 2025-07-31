package com.siddiqui.myapplication.utils

sealed class Screen(val route: String) {
    object Home: Screen("Home")

}