package com.siddiqui.myapplication.utils

fun main(){
println("siddiqui".firstChat())
}

fun String.getMiddleName(): String? {
    val parts = this.trim().split("\\s+".toRegex())
    return if (parts.size > 2) {
        parts.subList(1, parts.size - 1).joinToString(" ")
    } else {
        null
    }
}

fun String.firstCharOfWord(): String {
    return this.split(" ").joinToString(" ")
    {
        it.lowercase().replaceFirstChar { firstChar -> firstChar.uppercase() }
    }
}

fun String.firstChat(): String {
    return this[0].uppercase() + this.substring(1)
}

