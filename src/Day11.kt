import com.google.common.truth.Truth.assertThat
import java.math.BigInteger

class Monkey(
    val id: String,
    startingItems: List<Int>,
    private val inspectOperation: (BigInteger) -> BigInteger,
    private val testValue: BigInteger,
    private val onTrue: String,
    private val onFalse: String,
    private val debug: Boolean
) {
    private val heldItems: Array<BigInteger> = Array(40) { BigInteger.ZERO }

    private var itemCount = 0

    init {
        if (debug) {
            println("Init")
        }
        startingItems.forEach { giveItem(it.toBigInteger()) }
        if (debug) {
            printHoldings()
        }
    }

    var totalInspections: Long = 0
        private set

    private fun giveItem(item: BigInteger) {
        heldItems[itemCount] = item
        itemCount++
        if (debug) {
            println("$id given $item, now has $itemCount items")
        }
    }

    fun printHoldings() {
        if (debug) {
            println("$id holding $itemCount items: ${heldItems.take(itemCount).joinToString()}")
        }
    }

    private fun inspectPart1(item: Int) = inspectOperation(heldItems[item]) / BigInteger.valueOf(3)

    private fun takeTurn(monkeys: Map<String, Monkey>, inspection: (Int) -> BigInteger) {
        if (debug) {
            println("----")
        }
        for (i in 0 until itemCount) {
            totalInspections++
            val newValue = inspection(i)
            if ((newValue % testValue) == BigInteger.ZERO) {
                monkeys[onTrue]?.giveItem(newValue)
            } else {
                monkeys[onFalse]?.giveItem(newValue)
            }
        }
        if (debug) {
            println("----")
        }
        itemCount = 0
    }

    fun takePart1Turn(monkeys: Map<String, Monkey>) {
        takeTurn(monkeys, ::inspectPart1)
    }
}

fun String.toOperation(): (BigInteger) -> BigInteger {
    val (operation, operand) = split(" ")

    if (operand == "old") {
        if (operation == "*") {
            return { x -> x * x }
        } else {
            return { x -> x + x }
        }
    } else {
        val parsedOperand = operand.toBigInteger()
        if (operation == "*") {
            return { x -> x * parsedOperand }
        } else {
            return { x -> x + parsedOperand }
        }
    }
}

fun parseMonkeys(input: List<String>, debug: Boolean): Map<String, Monkey> {
    val monkeys = mutableMapOf<String, Monkey>()
    with(input.iterator()) {
        while (hasNext()) {
            val id = next().split(" ")[1].removeSuffix(":")
            val startingItems = next().removePrefix("  Starting items: ").split(", ").map { it.toInt() }
            val operation = next().removePrefix("  Operation: new = old ").toOperation()
            val test = next().removePrefix("  Test: divisible by ").toBigInteger()
            val trueTarget = next().removePrefix("    If true: throw to monkey ")
            val falseTarget = next().removePrefix("    If false: throw to monkey ")
            monkeys[id] = Monkey(id, startingItems, operation, test, trueTarget, falseTarget, debug)
            //Skip blank lines between monkeys
            if (hasNext()) {
                next()
            }
        }
    }
    if (debug) {
        println("Parsed ${monkeys.values.size} monkeys")
    }
    return monkeys
}

fun main() {
    fun calculateMonkeyBusiness(monkeys: Map<String, Monkey>) =
        monkeys.values.map { it.totalInspections }.sortedDescending().take(2).fold(1L) { acc, i -> acc * i }

    fun part1(input: List<String>, debug: Boolean): Long {
        val monkeys = parseMonkeys(input, debug)
        repeat(20) {
            monkeys.values.sortedBy { it.id }.forEach {
                it.takePart1Turn(monkeys)

            }
            if (debug) {
                println("-------------------------------------")
                monkeys.values.sortedBy { it.id }.forEach {
                    it.printHoldings()
                }
                println("-------------------------------------")
            }
        }
        return calculateMonkeyBusiness(monkeys)
    }

    val testInput = readInput("Day11_test")
    assertThat(part1(testInput, true)).isEqualTo(10605L)
    val input = readInput("Day11")
    println(part1(input, false))
}