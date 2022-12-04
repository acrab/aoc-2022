import com.google.common.truth.Truth.assertThat

fun scoreForItem(char: Char): Int =
    if (char in 'a'..'z') {
        (char.code - 97) + 1
    } else {
        (char.code - 65) + 27
    }

fun main() {
    fun part1(input: List<String>): Int = input
        //Split into two parts
        .map { it.chunked(it.length / 2) }
        //Find unique element
        .map { it[0].first { char -> char in it[1] } }
        //Score
        .sumOf { scoreForItem(it) }

    fun part2(input: List<String>): Int = input
        .chunked(3)
        .sumOf { group ->
            val (first, second, third) = group
            println("Testing $first, $second, $third")
            scoreForItem(first.filter { it in second }.filter { it in third }[0])

        }

    val testInput = readInput("Day03_test")
    assertThat(part1(testInput)).isEqualTo(157)
    val input = readInput("Day03")
    println("-- part 1 --")
    println(part1(input))
    assertThat(part2(testInput)).isEqualTo(70)
    println("-- part 2 --")
    println(part2(input))
}