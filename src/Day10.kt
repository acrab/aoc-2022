import com.google.common.truth.Truth.assertThat

class Computer(maxCycles: Int) {
    // Internal
    private var programCounter = 0

    private val screenBuffer = StringBuilder()

    // Registers
    private var x = 1

    // Output
    var signalSum = 0
        private set

    fun renderScreen() = screenBuffer.toString().trim()

    private fun init() {
        programCounter = 0
        x = 1
        signalSum = 0
        screenBuffer.clear()
    }

    private val framesToCount = 20..maxCycles step 40

    private fun tick() {
        programCounter += 1
        if (programCounter in framesToCount) {
            signalSum += x * programCounter
        }
        // Rendering
        if ((programCounter - 1) % 40 in x - 1..x + 1) {
            screenBuffer.append("#")
        } else {
            screenBuffer.append(".")
        }
        if ((programCounter % 40) == 0) {
            screenBuffer.append("\n")
        }
    }

    fun runProgram(program: List<String>) {
        init()
        program.forEach {
            when (it) {
                "noop" -> tick()
                else -> {
                    repeat(2) { tick() }
                    x += it.split(" ")[1].toInt()
                }
            }
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        with(Computer(input.size * 2)) {
            runProgram(input)
            return signalSum
        }
    }

    fun part2(input: List<String>): String {
        with(Computer(input.size * 2)) {
            runProgram(input)
            return renderScreen()
        }
    }

    val testInput = readInput("Day10_test")
    assertThat(part1(testInput)).isEqualTo(13140)
    val input = readInput("Day10")
    println(part1(input))

    assertThat(part2(testInput)).isEqualTo(
        """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
    """.trimIndent()
    )
    println(part2(input))
}