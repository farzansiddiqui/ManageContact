package com.siddiqui.myapplication.utils

import com.siddiqui.myapplication.model.BitCoinPayment
import com.siddiqui.myapplication.model.CreditCard
import com.siddiqui.myapplication.model.PaymentMethod
import com.siddiqui.myapplication.model.PaymentProcessor
import com.siddiqui.myapplication.model.Rectangle
import com.siddiqui.myapplication.model.Shape
import com.siddiqui.myapplication.model.Square
import com.siddiqui.myapplication.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import java.util.Stack
import kotlin.math.max


fun main() {
   repeatActions(3){
       println("Hello")
   }


        val sum = { a: Int, b: Int -> a + b }


    val numberAdd = addHighOrder(25,3,sum)
    println(numberAdd)

    val str = "Farzan".conCateNate(" Siddiqui")
    println(str)

    val p1 = Point(10,20)
    val p2 = Point(30,40)

    val p3 = p1 + p2
    println(p3)

    println(isPalindrome("madam"))

}

fun simpleFlow(): Flow<Int> = flow {
    for (i in 1..5){
        delay(100)
        emit(i)
    }
}

fun String.getMiddleName(): String? {
    val parts = this.trim().split("\\s+".toRegex())
    return if (parts.size > 2){
        parts.subList(1, parts.size - 1).joinToString(" ")
    }else {
        null
    }
}


val greet = {name: String-> println("Hello $name")}



data class Point(val x:Int, val b: Int){
    operator fun plus(other: Point):Point {
        return Point(other.x, other.b)
    }
}

fun String.conCateNate(str: String):String{
    return this + str
}

fun addHighOrder(a:Int, b:Int,add:(Int, Int)-> Int):Int {
    return add(a, b)
}

inline fun repeatActions(times:Int, action:()-> Unit){
    for (i in 0 until times){
        action()
    }
}



fun printString(str:String, myString:(String)-> String ):String {
    return myString(str)
}




interface Topic {
    fun understand()
}
class Topic1 : Topic {
    override fun understand() {
        println("Got it")
    }
}
class Topic2 : Topic {
    override fun understand() {
        println("Understand")
    }
}


fun isBalanced(s: String): Boolean {
    // Declare a stack to store the opening brackets

    val st = Stack<Char>()
    for (i in 0 until s.length) {
        // Check if the character is an opening bracket

        if (s[i] == '(' || s[i] == '{' || s[i] == '[') {
            st.push(s[i])
        } else {
            if (!st.empty() &&
                ((st.peek() == '(' && s[i] == ')') ||
                        (st.peek() == '{' && s[i] == '}') ||
                        (st.peek() == '[' && s[i] == ']'))
            ) {
                st.pop()
            } else {
                return false
            }
        }
    }


    // If stack is empty, return true (balanced),
    // otherwise false
    return st.empty()
}
fun printGroupBy(fruits: List<String>): List<String>{
    val value  = fruits.groupBy { it.first() }.map { it.value.first() }
    return value

}

fun isValidString(word: String): Boolean {
    if (word.length < 3) return false
    val conditions = mutableListOf<Boolean>(false,false)
    for (i in word){
        if (!(i in '0'..'9' || i in 'a'..'z' || i in 'A'.. 'Z')){
            return false
        }
        if (i in listOf('a','e','i','o','u') || i in listOf('A','E','I','U','O')) {
            conditions[0] = true
        }
        else if (i in 'a'..'z') conditions[1] = true
        else if (i in 'A'..'Z') conditions[1] = true
    }
    for (i in conditions){
        if (!i) return false
    }
    return true

}

fun isValid(word: String): Boolean {
    return word.length >= 3 && word.chars()
        .allMatch { codePoint: Int -> Character.isLetterOrDigit(codePoint) } &&
            word.chars().anyMatch { c: Int -> isVowel(c.toChar()) } &&
            word.chars().anyMatch { c: Int -> isConsonant(c.toChar()) }
}

 fun isVowel(c: Char): Boolean {
    return "aeiouAEIOU".indexOf(c) != -1
}

 fun isConsonant(c: Char): Boolean {
    return Character.isLetter(c) && !isVowel(c)
}

fun findMinDifference(nums: IntArray): Int{
        nums.sort()
    var diff = Integer.MAX_VALUE
    for (i in 0 until nums.size - 1) {
        if (nums[i +1] - nums[i] < diff){
            diff = nums[i + 1] - nums[i]
        }
    }
    return diff

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


fun isPrime(num: Int): Boolean {
    if (num <= 1) {
        return false
    }
    var i = 2
    while (i * i <= num) {
        if (num % i == 0) {
            return false
        }
        i++
    }
    return true
}

fun isPalindrome(str: String): Boolean {
    var i = 0
    var j = str.length - 1

    while (i < j){
        if (str[i] != str[j]){
            return false
        }
        i++
        j--
    }
    return true
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

fun printTwoArray(){
    val twoDArray = Array(3){ Array(4) {0} }
    println(twoDArray.contentDeepToString())
}

fun twoSum(nums: IntArray,target:Int): Boolean {
    for (i in 0 until   nums.size){
        for (j in i + 1 until  nums.size){
            if (nums[i] + nums[j] == target){
                return true
            }
        }
    }
    return false
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

fun removeElement(nums: IntArray):Int {
    var count = 0
    for (i in nums.indices){
        if (i < nums.size - 1 && nums[i] == nums[i + 1]){
            continue
        }else {
            nums[count] = nums[i]
            count++
        }
    }
    return count
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