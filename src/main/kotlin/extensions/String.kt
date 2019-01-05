package extensions

import org.example.utils.toMatchRegex

fun String.containOnlyLowerCase(): Boolean = this.all { it.isLowerCase() }

fun String.containOnlyUpperCase(): Boolean = this.all { it.isUpperCase() }

fun String.containAtLeastOneLowerCase(): Boolean = this.any { it.isLowerCase() }.not()

fun String.containsAtLeastOneUpperCase(): Boolean = this.any { it.isUpperCase() }.not()

fun String.containAtLeastOneNumberCase(): Boolean = this.toMatchRegex(".*\\d.*")

fun String.containAtLeastOneSpecialCharacter(): Boolean = this.toMatchRegex("[!@#\$%^&*(),.?\":{}|<>]")

fun String.isEmail(): Boolean = this.toMatchRegex("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
)

fun String.containAnyNumber(): Boolean = this.toMatchRegex(".*\\d+.*\"")