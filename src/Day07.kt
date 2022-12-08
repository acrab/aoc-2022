import com.google.common.truth.Truth.assertThat

fun main() {
    fun calculateDirectorySize(
        currentPath: String,
        iterator: Iterator<String>,
        directoryMap: MutableMap<String, Int>
    ): Int {
        var totalFileSize = 0
        val subdirectories = mutableMapOf<String, Int>()

        while (iterator.hasNext()) {
            val command = iterator.next()
            if (command == "$ cd ..") {
                //Navigating up
                val totalSize = totalFileSize + subdirectories.values.sum()
                directoryMap[currentPath] = totalSize
                return totalSize
            } else if (command.startsWith("$ cd")) {
                //moving to a subdirectory
                val subDirectory = command.split(" ").last()
                subdirectories[subDirectory] =
                    calculateDirectorySize("$currentPath/$subDirectory", iterator, directoryMap)
            } else {
                //File listing
                val size = command.split(" ").first()
                if (size.first().isDigit()) {
                    //It's a file
                    totalFileSize += size.toInt()
                }
                //Otherwise it's a directory, and we can skip it.
            }
        }

        //End of command output
        val totalSize = totalFileSize + subdirectories.values.sum()
        directoryMap[currentPath] = totalSize
        return totalSize
    }

    fun calculateFullRepository(input: List<String>): Map<String, Int> {
        val directory = mutableMapOf<String, Int>()
        val iterator: Iterator<String> = input.iterator()
        val root = iterator.next().split(" ").last()
        println("Setting root to $root")
        calculateDirectorySize(root, iterator, directory)
        println(directory)
        return directory
    }

    fun part1(input: List<String>): Int {
        //Path to size
        val directory = calculateFullRepository(input)
        return directory.values.filter { it <= 100000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val maxSize = 70000000
        val updateSize = 30000000

        val directory = calculateFullRepository(input)
        val totalFileSize = directory["/"]!!
        val freeSpace = maxSize - totalFileSize
        val needToFree = updateSize - freeSpace

        return directory.values.filter { it >= needToFree }.minOf { it }
    }

    val testInput = readInput("Day07_test")
    assertThat(part1(testInput)).isEqualTo(95437)
    val input = readInput("Day07")
    println(part1(input))

    assertThat(calculateFullRepository(testInput)["/"]).isEqualTo(48381165)

    assertThat(part2(testInput)).isEqualTo(24933642)
    println(part2(input))
}