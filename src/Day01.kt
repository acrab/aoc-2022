import com.google.common.truth.Truth.assertThat

fun main() {
    fun part1(input: List<String>): Int {
        var max = 0
        var current = 0
        for (line in input) {
            if (line.isEmpty()) {
                if (current > max) {
                    max = current
                }
                current = 0
            } else {
                current += line.toInt()
            }
        }
        if (current > max) {
            max = current
        }
        return max
    }

    fun part2(input: List<String>): Int {
        val groups = mutableListOf<Int>()
        var current = 0
        for (line in input) {
            if (line.isEmpty()) {
                groups.add(current)
                current = 0
            } else {
                current += line.toInt()
            }
        }
        groups.add(current)
        return groups.sortedDescending().take(3).reduce { a, b -> a + b }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    assertThat(part1(testInput)).isEqualTo(24000)
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    assertThat(part2(testInput)).isEqualTo(45000)

    println(part2(input))
}
