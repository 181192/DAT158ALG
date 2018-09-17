import kotlinx.coroutines.experimental.launch
import kotlin.system.measureTimeMillis

val max = { a: Int, b: Int ->
    maxOf(a, b)
}

val altMax: (a: Int, b: Int) -> Int = Math::max

fun lcs(a: String, b: String): String {

    val bLen = b.length - 1
    val aLen = a.length - 1
    return when {
        a.isEmpty() || b.isEmpty() -> ""
        a[aLen] == b[bLen] -> lcs(a.substring(0, aLen), b.substring(0, bLen)) + a[aLen]
        else -> {
            val x = lcs(a, b.substring(0, bLen))
            val y = lcs(a.substring(0, aLen), b)
            if (x.length > y.length) x else y
        }
    }
}

fun lcsDynamic(a: String, b: String): String {
    val lengths = Array(a.length + 1) { IntArray(b.length + 1) }

    // row 0 and column 0 are initialized to 0 already

    for (i in 0 until a.length)
        for (j in 0 until b.length)
            if (a[i] == b[j])
                lengths[i + 1][j + 1] = lengths[i][j] + 1
            else
                lengths[i + 1][j + 1] = Math.max(lengths[i + 1][j], lengths[i][j + 1])

    // read the substring out from the matrix
    val sb = StringBuffer()
    var x = a.length
    var y = b.length
    while (x != 0 && y != 0) {
        when {
            lengths[x][y] == lengths[x - 1][y] -> x--
            lengths[x][y] == lengths[x][y - 1] -> y--
            else -> {
                assert(a[x - 1] == b[y - 1])
                sb.append(a[x - 1])
                x--
                y--
            }
        }
    }
    return sb.reverse().toString()
}

fun main(args: Array<String>) {
    val a = "aabbaaaaabaaaabaadaaacaacccaadaabbaaabcccdddaasa"
    val b = "aabbaaab"


    println(measureTimeMillis {
        lcs(a, b)
    })
    println(measureTimeMillis {
        lcsDynamic(a, b)
    })
}