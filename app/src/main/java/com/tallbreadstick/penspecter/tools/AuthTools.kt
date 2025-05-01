package com.tallbreadstick.penspecter.tools

import android.content.Context
import android.widget.Toast

fun validateUsername(username: String): Boolean {
    val regex = "^[a-zA-Z0-9._]+$"
    return username.matches(regex.toRegex()) && username.length > 3 && username.isNotBlank()
}

fun validatePassword(password: String): Boolean {
    val regex = "^[\\x00-\\x7F]+$"
    return password.matches(regex.toRegex()) && password.length > 3 && password.isNotBlank()
}

fun validateEmail(email: String): Boolean {
    val regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    return email.matches(regex.toRegex())
}

fun testPasswordStrength(password: String): PasswordStrength {
    if (password.length < 8 || password.all { it.isLetterOrDigit() }) {
        return PasswordStrength.Weak
    }
    if (password.any { it.isDigit() } && password.any { it.isLetter() }) {
        return PasswordStrength.Fair
    }
    if (password.length >= 12 &&
        password.any { it.isDigit() } &&
        password.any { it.isLetter() } &&
        password.any { !it.isLetterOrDigit() }) {
        return PasswordStrength.Strong
    }
    return PasswordStrength.Weak
}

enum class PasswordStrength(value: Int) {
    Weak(1),
    Fair(2),
    Strong(3)
}

suspend fun registerUser(context: Context, username: String, email: String, password: String): Boolean {
    val db = DatabaseProvider.getDatabase(context)
    val userDao = db.userDao()

    // Check if user already exists
    val existingUser = userDao.getUser(username, password)
    if (existingUser != null) {
        Toast.makeText(context, "User already exists!", Toast.LENGTH_LONG).show()
        return false
    }

    // Insert new user
    val user = UserEntity(username, email, password)
    userDao.insert(user)
    Toast.makeText(context, "Registered successfully!", Toast.LENGTH_LONG).show()
    return true
}

suspend fun loginUser(context: Context, username: String, password: String): Boolean {
    val db = DatabaseProvider.getDatabase(context)
    val userDao = db.userDao()

    val user = userDao.getUser(username, password)
    return if (user != null) {
        Toast.makeText(context, "Logged in successfully!", Toast.LENGTH_LONG).show()
        true
    } else {
        Toast.makeText(context, "Invalid username or password", Toast.LENGTH_LONG).show()
        false
    }
}