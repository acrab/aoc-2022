import com.google.common.truth.Truth.assertThat

fun main() {

    fun List<String>.parseList(): List<List<List<Int>>> =
        map { it.split(",").map { sublist -> sublist.split("-").map(String::toInt) } }

    fun part1(input: List<String>): Int =
        input.parseList()
            .count { (it[0][0] <= it[1][0] && it[0][1] >= it[1][1]) || (it[1][0] <= it[0][0] && it[1][1] >= it[0][1]) }


    fun part2(input: List<String>): Int =
        input.parseList()
            .count {
                it[0][0] in it[1][0]..it[1][1] || it[0][1] in it[1][0]..it[1][1] ||
                        it[1][0] in it[0][0]..it[0][1] || it[1][1] in it[0][0]..it[0][1]
            }


    val testInput = readInput("Day04_test")
    assertThat(part1(testInput)).isEqualTo(2)
    val input = readInput("Day04")
    println(part1(input))

    assertThat(part2(testInput)).isEqualTo(4)
    println(part2(input))
}