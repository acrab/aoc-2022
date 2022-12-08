import com.google.common.truth.Truth.assertThat

fun main() {
    fun isVisible(height: Int, x: Int, y: Int, map: List<List<Int>>): Boolean {
        //edge trees are always visible
        if (x == 0 || x == map.size - 1 || y == 0 || y == map[x].size - 1) {
            return true
        }
        //check up
        var visibleUp = true
        for (i in x - 1 downTo 0) {
            if (map[i][y] >= height) visibleUp = false
        }

        //check down
        var visibleDown = true
        for (i in x + 1 until map.size) {
            if (map[i][y] >= height) visibleDown = false
        }

        //check left
        var visibleLeft = true
        for (i in y - 1 downTo 0) {
            if (map[x][i] >= height) visibleLeft = false
        }

        //check right
        var visibleRight = true
        for (i in y + 1 until map.size) {
            if (map[x][i] >= height) visibleRight = false
        }

        return visibleDown || visibleUp || visibleLeft || visibleRight
    }

    fun part1(input: List<String>): Int {
        //Convert them all to integers
        val map = input.map { line -> line.map { it.digitToInt() } }
        val visibility = map.mapIndexed { x, rows -> rows.mapIndexed { y, tree -> isVisible(tree, x, y, map) } }
        return visibility.sumOf { line -> line.count { it } }
    }

    fun calculateVisibility(height: Int, x: Int, y: Int, map: List<List<Int>>): Int {
        //check up
        var visibleUp = 0
        for (i in x - 1 downTo 0) {
            visibleUp += 1
            if (map[i][y] >= height) break
        }

        //check down
        var visibleDown = 0
        for (i in x + 1 until map.size) {
            visibleDown += 1
            if (map[i][y] >= height) break
        }

        //check left
        var visibleLeft = 0
        for (i in y - 1 downTo 0) {
            visibleLeft += 1
            if (map[x][i] >= height) break
        }

        //check right
        var visibleRight = 0
        for (i in y + 1 until map.size) {
            visibleRight += 1
            if (map[x][i] >= height) break
        }

        return visibleDown * visibleUp * visibleLeft * visibleRight
    }


    fun part2(input: List<String>): Int {
        //Convert them all to integers
        val map = input.map { line -> line.map { it.digitToInt() } }
        val visibility =
            map.mapIndexed { x, rows -> rows.mapIndexed { y, tree -> calculateVisibility(tree, x, y, map) } }
        return visibility.maxOf { line -> line.max() }
    }

    val testInput = readInput("Day08_test")
    assertThat(part1(testInput)).isEqualTo(21)
    val input = readInput("Day08")
    println(part1(input))

    assertThat(part2(testInput)).isEqualTo(8)
    println(part2(input))
}