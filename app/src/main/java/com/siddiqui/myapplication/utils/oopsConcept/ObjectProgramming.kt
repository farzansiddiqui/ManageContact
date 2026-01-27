package com.siddiqui.myapplication.utils.oopsConcept


fun main() {
    val employee1 = Employee("Farzan")
    val employee2 = Employee("Hassan")
    val employee3 = Employee("Siddiqui")

    val bank = Bank("SBI", mutableSetOf(employee1, employee2, employee3))
    println(bank)

}

data class Bank(val bankName: String, var employee: MutableSet<Employee>)
data class Employee(val name: String)