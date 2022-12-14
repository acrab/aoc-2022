import com.google.common.truth.Truth.assertThat
import java.lang.Integer.max
import java.lang.Integer.min

enum class GridSpace { Rock, Air, Sand }

fun rangeBetween(start: Int, end: Int) = min(start, end)..max(start, end)

fun main() {

    fun List<MutableList<GridSpace>>.printMap(width: Int) {
        println(joinToString("\n") { row ->
            row.takeLast(width).joinToString("") {
                when (it) {
                    GridSpace.Rock -> "#"
                    GridSpace.Air -> " "
                    GridSpace.Sand -> "o"
                }
            }
        })
    }


    fun MutableList<MutableList<GridSpace>>.fillLine(startX: Int, startY: Int, endX: Int, endY: Int) {
        while (size <= max(startY, endY)) {
            add(mutableListOf())
        }
        for (y in rangeBetween(startY, endY)) {
            val row = this[y]
            while (row.size <= max(startX, endX)) {
                row.add(GridSpace.Air)
            }
            for (x in rangeBetween(startX, endX)) {
                row[x] = GridSpace.Rock
            }
        }
    }

    /**
     * Produces a grid indexed &#91;y&#93;&#91;x&#93;
     */
    fun parseGrid(input: List<String>): MutableList<MutableList<GridSpace>> {
        val output = mutableListOf<MutableList<GridSpace>>()
        input.forEach { shape ->
            shape.split(" -> ")
                .windowed(2)
                .forEach { line ->
                    val (startX, startY) = line[0].split(",").map { it.toInt() }
                    val (endX, endY) = line[1].split(",").map { it.toInt() }
                    output.fillLine(startX, startY, endX, endY)
                }
        }

        val maxLength = output.maxOf { it.size }
        output.forEach {
            while (it.size < maxLength) {
                it.add(GridSpace.Air)
            }
        }

        return output
    }

    fun MutableList<GridSpace>.addSandGrain(position: Int) {
        while (size < position) {
            add(GridSpace.Air)
        }
        set(position, GridSpace.Sand)
    }

    fun part1(input: List<String>): Int {
        val map = parseGrid(input)
        map.printMap(200)

        var sandGrainsAdded = 0
        val path = mutableListOf(0 to 500)
        while (true) {
            val (currentY, currentX) = path.last()
            val nextRowCoordinate = currentY + 1
            //If a sand grain goes below this point, it has fallen off the map, and we can end

            if (nextRowCoordinate == map.size) {
                break
            }
            val nextRow = map[nextRowCoordinate]
            //try to fall down
            if (currentX > nextRow.size || nextRow[currentX] == GridSpace.Air) {
                path.add(nextRowCoordinate to currentX)
                continue
            }

            //try to fall left
            if (currentX - 1 > nextRow.size || nextRow[currentX - 1] == GridSpace.Air) {
                path.add(nextRowCoordinate to currentX - 1)
                continue
            }
            //try to fall right
            if (currentX + 1 > nextRow.size || nextRow[currentX + 1] == GridSpace.Air) {
                path.add(nextRowCoordinate to currentX + 1)
                continue
            }
            //can't fall in any direction, then place a grain, and start next grain from the row above
            sandGrainsAdded++
            path.removeLast()
            map[currentY].addSandGrain(currentX)
        }

        return sandGrainsAdded
    }

    fun part2(input: List<String>): Int {
        val map = parseGrid(input)
        val mapWidth = map[0].size
        map.add(MutableList(mapWidth) { GridSpace.Air })
        val extraSpace = MutableList(100) { GridSpace.Air }
        map.forEach { it.addAll(extraSpace) }
        map.printMap(200)

        var sandGrainsAdded = 0
        val path = mutableListOf(0 to 500)
        while (path.any()) {
            if (sandGrainsAdded > 0 && sandGrainsAdded % 5_000 == 0) {
                println("Added $sandGrainsAdded")
                map.printMap(200)
            }
            val (currentY, currentX) = path.last()
            val nextRowCoordinate = currentY + 1
            if (nextRowCoordinate >= map.size) {
                //we've reached the floor, place a grain and continue
                sandGrainsAdded++
                path.removeLast()
                map[currentY].addSandGrain(currentX)
                continue
            }
            val nextRow = map[nextRowCoordinate]
            //try to fall down
            if (currentX >= nextRow.size || nextRow[currentX] == GridSpace.Air) {
                path.add(nextRowCoordinate to currentX)
                continue
            }

            //try to fall left
            if (currentX - 1 >= nextRow.size || nextRow[currentX - 1] == GridSpace.Air) {
                path.add(nextRowCoordinate to currentX - 1)
                continue
            }
            //try to fall right
            if (currentX + 1 >= nextRow.size || nextRow[currentX + 1] == GridSpace.Air) {
                path.add(nextRowCoordinate to currentX + 1)
                continue
            }
            //can't fall in any direction, then place a grain, and start next grain from the row above
            sandGrainsAdded++
            path.removeLast()
            map[currentY].addSandGrain(currentX)
        }

        return sandGrainsAdded
    }

    val testInput = readInput("Day14_test")
    assertThat(part1(testInput)).isEqualTo(24)
    val input = readInput("Day14")
    println(part1(input))

    assertThat(part2(testInput)).isEqualTo(93)
    println(part2(input))
}