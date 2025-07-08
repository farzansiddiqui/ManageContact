package com.siddiqui.myapplication.utils

import kotlin.math.max


fun main() {
    val charArray = charArrayOf('a','a','b','b','c','c','c')
    println(reverseWordsUsingArray("Farzan Hassan Siddiqui"))


}



fun countOrder(str: String): String{
    val result = StringBuilder()
    val hashMap = mutableMapOf<Char, Int>()

    for (i in str.indices){
        hashMap.put(str[i], hashMap.getOrDefault(str[i], 0)+1)
    }
    for ((key,value ) in hashMap){
        result.append(key).append(value)
    }
    return result.toString()
}

fun compress(chars: CharArray):Int {
    if (chars.isEmpty()) return chars.size

    var index = 0
    var i = 0
    while (i < chars.size){
        val currentChar = chars[i]
        var count = 0
        while (i < chars.size && chars[i] == currentChar){
            i++
            count++
        }
        chars[index++] = currentChar
        if (count != 1){
            for (c in count.toString().toCharArray()) {
                chars[index++] = c
            }
        }
    }
    return index

}


fun enCoding(str: String): String {
    if (str.isEmpty()) return  "" //"wwwwaaadexxxxxxywww"
    val result = StringBuilder()
    var currentChar = str[0]
    var count = 1
    for (i in 1 until str.length) {
        if (str[i] == currentChar) {
            count++
        } else {
            result.append(currentChar).append(count) //
            currentChar = str[i]
            count = 1
        }
    }
    // Add the last character sequence

    result.append(currentChar).append(count)


    return result.toString()
}

fun removeDuplicates(nums: IntArray): Int {
    var uniqueIndex = 0

    for (i in nums.indices) {
        var isDuplicate = false
        for (j in 0 until uniqueIndex) {
            if (nums[i] == nums[j]) {
                isDuplicate = true
                break
            }
        }
        if (!isDuplicate) {
            nums[uniqueIndex] = nums[i]
            uniqueIndex++
        }
    }

    return uniqueIndex
}

fun lengthOfLongestSubstring(s: String): Int {
    var count = 0
    val n = s.length
    val isNotRepeated = BooleanArray(26)
    if (n > 1) {
        for (i in 0 until n) {
            isNotRepeated.fill(false)
            for (j in i until n) {
                if (isNotRepeated[s[j].code - 'a'.code]) {
                    break
                } else {
                    count = max(count, j - i + 1)
                    isNotRepeated[s[j].code - 'a'.code] = true
                }
            }


        }
    }


    return count
}

fun longestUniqueSubstr(s: String): Int {
    var left = 0
    var maxLength = 0
    val seenChars = mutableSetOf<Char>()

    for (right in s.indices) { // geeksforgeeks left = 0 , maxL = 0 right = 0
        while (seenChars.contains(s[right])) {
            seenChars.remove(s[left])
            left++
        }
        seenChars.add(s[right]) // ge
        maxLength = maxOf(maxLength, right - left + 1)
    }

    return maxLength
}

fun reverseWords(str: String):String { // Farzan Hassan Siddiqui
    val res = StringBuilder()
    val convertArray = str.split(" ")
    for (i in convertArray.size - 1 downTo 0){
        res.append(convertArray[i])
        if (i != 0){
            res.append(" ")
        }
    }
    return  res.toString()
}

fun reverseWordsUsingArray(str: String):String {
    val words  = str.split(" ")
    return words.reversed().joinToString(" ")
}