package com.siddiqui.myapplication.model

class User private constructor(builder: Builder) {
    private val name: String?
    private val age: Int
    private val email: String?

    init {
        this.name = builder.name
        this.age = builder.age
        this.email = builder.email
    }

    class Builder {
        var name: String? = null
        var age = 0
        var email: String? = null

        fun setName(name: String?): Builder {
            this.name = name
            return this
        }

        fun setAge(age: Int): Builder {
            this.age = age
            return this
        }

        fun setEmail(email: String?): Builder {
            this.email = email
            return this
        }

        fun build(): User {
            return User(this)
        }
    }
}
