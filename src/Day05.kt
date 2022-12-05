import com.google.common.truth.Truth.assertThat

fun main() {
    fun part1(input: List<String>): String {
        val (moves, initialCrates) = input.partition { line -> line.any { it.isDigit() } }
        //Build crates
        val crates = mutableListOf<MutableList<Char>>()
        for (pile in initialCrates) {
            crates.add(
                pile.split(",").reversed().map { it[0] }.toMutableList()
            )
        }
        println("Initial state")
        println(crates)
        //do moves
        moves.forEach {
            val parts = it.split(" ")
            val toMove = parts[1].toInt()
            val from = crates[parts[3].toInt() - 1]
            val to = crates[parts[5].toInt() - 1]
            println("Moving $toMove from $from to $to")
            repeat(toMove) {
                to.add(from.removeLast())
            }
            println(crates)
        }
        println("Final state")
        println(crates)
        return crates.map { it.last() }.joinToString(separator = "")
    }

    fun part2(input: List<String>): String {
        val (moves, initialCrates) = input.partition { line -> line.any { it.isDigit() } }
        //Build crates
        val crates = mutableListOf<MutableList<Char>>()
        for (pile in initialCrates) {
            crates.add(
                pile.split(",").reversed().map { it[0] }.toMutableList()
            )
        }
        println("Initial state")
        println(crates)
        //do moves
        moves.forEach {
            val parts = it.split(" ")
            val toMove = parts[1].toInt()
            val from = crates[parts[3].toInt() - 1]
            val to = crates[parts[5].toInt() - 1]
            val movedCrates = from.takeLast(toMove)
            crates[parts[3].toInt() - 1] = from.take(from.size - toMove).toMutableList()
            to.addAll(movedCrates)
            println(crates)
        }
        println("Final state")
        println(crates)
        return crates.map { it.last() }.joinToString(separator = "")
    }

    val testInput = readInput("Day05_test")
    assertThat(part1(testInput)).isEqualTo("CMZ")
    val input = readInput("Day05")
    println(part1(input))

    assertThat(part2(testInput)).isEqualTo("MCD")
    println(part2(input))
}