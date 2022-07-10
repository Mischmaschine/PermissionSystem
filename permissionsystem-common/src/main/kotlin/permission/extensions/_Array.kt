package permission.extensions

fun Array<out String>.getIndexFromString(string: String): Int? {
    for (i in 0 until this.size) {
        if (this[i] == string) {
            return i
        }
    }
    return null
}