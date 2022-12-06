import com.google.common.truth.Truth.assertThat

fun main() {
    fun allUnique(list: List<Char>): Boolean {
        for (i in 0..list.size-2) {
            for (j in (i+1) until list.size) {
                if (list[i] == list[j]) return false
            }
        }
        return true
    }

    fun findUniqueString(input: String, windowLength: Int): Int{
        val last4 = input.take(windowLength-1).toMutableList()
        var index = windowLength
        for (c in input.drop(windowLength-1)) {
            last4.add(c)
            if (allUnique(last4)) {
                return index
            }
            last4.removeAt(0)
            index++
        }

        return -1
    }

    fun part1(input: String): Int {
        return findUniqueString(input, 4)
    }

    fun part2(input: String): Int {
        return findUniqueString(input, 14)
    }

    val testInput = readInput("Day06_test")
    assertThat(part1(testInput[0])).isEqualTo(7)
    assertThat(part1(testInput[1])).isEqualTo(5)
    assertThat(part1(testInput[2])).isEqualTo(6)
    assertThat(part1(testInput[3])).isEqualTo(10)
    assertThat(part1(testInput[4])).isEqualTo(11)
    val input = readInput("Day06")[0]
    println(part1(input))

    assertThat(part2(testInput[0])).isEqualTo(19)
    assertThat(part2(testInput[1])).isEqualTo(23)
    assertThat(part2(testInput[2])).isEqualTo(23)
    assertThat(part2(testInput[3])).isEqualTo(29)
    assertThat(part2(testInput[4])).isEqualTo(26)
    println(part2(input))
}