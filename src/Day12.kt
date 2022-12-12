import com.google.common.truth.Truth.assertThat
import kotlin.math.min

data class HillPoint(val elevation: Char, val x: Int, val y: Int, val isEnd: Boolean = false) {
    var steps: Int = Int.MAX_VALUE
    var visited: Boolean = false

}

fun main() {
    fun parseMap(
        input: List<String>
    ): Pair<List<List<HillPoint>>, Pair<HillPoint, HillPoint>> {
        lateinit var startPoint: HillPoint
        lateinit var endPoint: HillPoint
        val map =
            input.mapIndexed { x, it ->
                it.toCharArray().mapIndexed { y, char ->
                    when (char) {
                        'S' -> {
                            startPoint = HillPoint('a', x, y)
                            startPoint
                        }

                        'E' -> {
                            endPoint = HillPoint('z', x, y, isEnd = true)
                            endPoint
                        }

                        else -> HillPoint(char, x, y)
                    }
                }
            }
        return Pair(map, startPoint to endPoint)
    }

    fun part1(input: List<String>): Int {
        val (map, keyPoints) = parseMap(input)
        val startPoint = keyPoints.first
        val pointsToCheck = mutableListOf(startPoint)

        startPoint.visited = true
        startPoint.steps = 0

        while (pointsToCheck.size > 0) {
            val nextPoint = pointsToCheck.minBy { it.steps }

            // remove ourselves
            pointsToCheck.remove(nextPoint)
            // Find all valid neighbours
            map.orthogonalNeighbours(nextPoint.x, nextPoint.y, null).filterNotNull()
                .forEach {
                    if (it.elevation <= nextPoint.elevation + 1) {
                        // Update their step counts
                        it.steps = min(it.steps, nextPoint.steps + 1)
                        // add them to the list if necessary
                        if (!it.visited) {
                            pointsToCheck.add(it)
                            it.visited = true

                            if (it.isEnd) {
                                return it.steps
                            }
                        }
                    }
                }
        }
        throw IllegalStateException("Didn't find end!")
    }

    fun part2(input: List<String>): Int {
        val (map, keyPoints) = parseMap(input)
        val endPoint = keyPoints.second
        val pointsToCheck = mutableListOf(endPoint)

        endPoint.visited = true
        endPoint.steps = 0

        while (pointsToCheck.size > 0) {
            val nextPoint = pointsToCheck.minBy { it.steps }

            // remove ourselves
            pointsToCheck.remove(nextPoint)
            // Find all valid neighbours
            map.orthogonalNeighbours(nextPoint.x, nextPoint.y, null).filterNotNull()
                .forEach {
                    if (it.elevation >= nextPoint.elevation - 1) {
                        // Update their step counts
                        it.steps = min(it.steps, nextPoint.steps + 1)
                        // add them to the list if necessary
                        if (!it.visited) {
                            pointsToCheck.add(it)
                            it.visited = true
                        }
                    }
                }
        }

        return map.minOf { row -> row.filter { it.elevation == 'a' }.minOf { it.steps } }
    }

    val testInput = readInput("Day12_test")
    assertThat(part1(testInput)).isEqualTo(31)
    val input = readInput("Day12")
    println(part1(input))

    assertThat(part2(testInput)).isEqualTo(29)
    println(part2(input))
}