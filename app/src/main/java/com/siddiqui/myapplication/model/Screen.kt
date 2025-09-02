package com.siddiqui.myapplication.model

sealed class Screen(val route: String, val title: String) {
    object  Record : Screen("record_screen","Record")
    object  Player : Screen("record_player", "Player")
    object  Setting : Screen("record_setting","Settings")

}