package com.siddiqui.myapplication.model



interface Shape {
    fun getArea(): Int
}

class Rectangle(val width:Int, val height: Int) : Shape {
    override fun getArea(): Int {
        return width * height
    }
}

class Square(val sideLength: Int): Shape{
    override fun getArea(): Int {
       return sideLength * sideLength
    }
}

interface PaymentMethod {
    fun process(amount: Double)
}

class CreditCard: PaymentMethod {
    override fun process(amount: Double) {
        println("implement credit card")
    }
}

class DebitCard : PaymentMethod {
    override fun process(amount: Double) {
        println("implement debit card")
    }
}

class BitCoinPayment: PaymentMethod {
    override fun process(amount: Double) {
        println("implement bitcoin payment")
    }
}
class PaymentProcessor {
    fun processPayment(paymentMethod: PaymentMethod, amount: Double){
        paymentMethod.process(200.5)
    }
}