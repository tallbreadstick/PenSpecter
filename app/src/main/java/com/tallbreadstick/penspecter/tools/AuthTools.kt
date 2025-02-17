package com.tallbreadstick.penspecter.tools

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