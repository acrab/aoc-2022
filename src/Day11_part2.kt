import com.google.common.truth.Truth.assertThat

class Item(private val startingValue: Int, debug: Boolean) {

    init {
        if (debug) {
            println("Created debug item with value $startingValue")
        }
    }

    data class Mod(
        val denominator: Int,
        var result: Int
    ) {
        fun increment(inc: Int) {
            result = (result + inc) % denominator
        }

        fun multiplyBy(mul: Int) {
            result = if (mul == 1) {
                result * result
            } else {
                (result * mul)
            } % denominator
        }
    }

    private lateinit var mods: MutableMap<String, Mod>

    fun setMonkeys(monkeys: Collection<Monkey2>) {
        mods = monkeys.associate {
            it.id to Mod(it.testValue, startingValue % it.testValue)
        }.toMutableMap()
    }

    fun incrementBy(inc: Int) {
        mods.values.forEach { it.increment(inc) }
    }

    fun multiplyBy(inc: Int) {
        mods.values.forEach { it.multiplyBy(inc) }
    }

    fun inspect(monkeyID: String): Boolean {
        return mods[monkeyID]?.result == 0
    }
}

enum class Action {
    Add, Multiply;

    companion object {
        fun fromString(string: String) =
            when (string) {
                "*" -> Multiply
                "+" -> Add
                else -> throw IllegalStateException("Unhandled operator $string")
            }
    }
}

class Monkey2(
    val id: String,
    val testValue: Int,
    private val deltaWorry: Int,
    private val action: Action,
    private val onTrue: String,
    private val onFalse: String,
    startingItems: List<Item>
) {
    private val heldItems: MutableList<Item>

    init {
        heldItems = startingItems.toMutableList()
    }

    var totalInspections: Long = 0
        private set

    private lateinit var passOnTrue: Monkey2
    private lateinit var passOnFalse: Monkey2

    fun setMonkeys(monkeys: Collection<Monkey2>) {
        passOnTrue = monkeys.first { it.id == onTrue }
        passOnFalse = monkeys.first { it.id == onFalse }
    }

    private fun giveItem(item: Item) {
        heldItems.add(item)
    }

    private fun inspect(item: Item): Boolean {

        if (action == Action.Add) {
            item.incrementBy(deltaWorry)
        } else {
            item.multiplyBy(deltaWorry)
        }
        totalInspections += 1
        return item.inspect(id)
    }

    fun takeTurn() {
        heldItems.forEach { item ->
            if (inspect(item)) {
                passOnTrue.giveItem(item)
            } else {
                passOnFalse.giveItem(item)
            }
        }
        heldItems.clear()
    }
}

fun parseByString(string: String) =
    if (string == "old") 1 else string.toInt()

fun parseMonkeysAndItems(input: List<String>): Map<String, Monkey2> {
    val monkeys = mutableMapOf<String, Monkey2>()
    val items = mutableListOf<Item>()
    var debugItem = false
    with(input.iterator()) {
        while (hasNext()) {
            val id = next().split(" ")[1].removeSuffix(":")
            val startingItems = next().removePrefix("  Starting items: ").split(", ").map {
                val i = Item(it.toInt(), debugItem)
                debugItem = false
                i
            }
            val (op, by) = next().removePrefix("  Operation: new = old ").split(" ")
            val operation = Action.fromString(op)
            val test = next().removePrefix("  Test: divisible by ").toInt()
            val trueTarget = next().removePrefix("    If true: throw to monkey ")
            val falseTarget = next().removePrefix("    If false: throw to monkey ")

            monkeys[id] = Monkey2(id, test, parseByString(by), operation, trueTarget, falseTarget, startingItems)

            items.addAll(startingItems)

            //Skip blank lines between monkeys
            if (hasNext()) {
                next()
            }
        }
    }
    items.forEach {
        it.setMonkeys(monkeys.values)
    }
    for (value in monkeys.values) {
        value.setMonkeys(monkeys.values)
    }

    return monkeys
}

fun main() {
    fun part2(input: List<String>): Long {
        val monkeys = parseMonkeysAndItems(input)
        val sortedMonkeys = monkeys.values.sortedBy { it.id }
        repeat(10_000) { round ->
            sortedMonkeys.forEach {
                it.takeTurn()
            }
            if (round + 1 in listOf(1, 20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10_000)) {
                println("${round + 1} - " + sortedMonkeys.joinToString { "${it.id}: ${it.totalInspections}" })
            }
        }
        return sortedMonkeys.map { it.totalInspections }.sortedDescending().take(2).fold(1L) { acc, i -> acc * i }
    }

    val testInput = readInput("Day11_test")
    assertThat(part2(testInput)).isEqualTo(2713310158L)

    val input = readInput("Day11")
    println(part2(input))
}