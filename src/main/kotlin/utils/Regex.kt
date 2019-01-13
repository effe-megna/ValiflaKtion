package utils

fun String.toMatchRegex(pattern: String): Boolean {
    return Regex(pattern).matches(this)
}