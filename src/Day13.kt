import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.*

enum class ItemOrdering {
    Correct, Incorrect, Undecided
}

fun main() {

    fun compareIntegers(left: Int, right: Int): ItemOrdering =
        when {
            left == right -> ItemOrdering.Undecided
            left < right -> ItemOrdering.Correct
            else -> ItemOrdering.Incorrect
        }

    fun compareJsonArrays(left: List<JsonElement>, right: List<JsonElement>): ItemOrdering {
        val comparison = left.zip(right) { l, r ->
            if (l is JsonPrimitive && r is JsonPrimitive) {
                compareIntegers(l.int, r.int)
            } else if (l is JsonArray && r is JsonArray) {
                compareJsonArrays(l, r)
            } else {
                //exactly one is an array, the other is a number
                if (l is JsonPrimitive) {
                    compareJsonArrays(listOf(l), r.jsonArray)
                } else {
                    compareJsonArrays(l.jsonArray, listOf(r))
                }
            }
        }

        return if (comparison.all { it == ItemOrdering.Undecided }) {
            when {
                left.size == right.size -> ItemOrdering.Undecided
                left.size < right.size -> ItemOrdering.Correct
                else -> ItemOrdering.Incorrect
            }
        } else {
            comparison.first { it != ItemOrdering.Undecided }
        }
    }

    fun part1(input: List<String>): Int =
        //3 to consume blank lines between groups
        input.chunked(3)
            .mapIndexed { index, strings ->
                val left = Json.parseToJsonElement(strings[0]).jsonArray
                val right = Json.parseToJsonElement(strings[1]).jsonArray

                if (compareJsonArrays(left, right) == ItemOrdering.Correct) {
                    index + 1
                } else {
                    0
                }
            }.sum()

    fun part2(input: List<String>): Int {
        val inputWithDividers = input + "[[2]]" + "[[6]]"
        println("Starting list\n${inputWithDividers.joinToString(separator = "\n")}\n-----")
        val filteredList = inputWithDividers.filterNot { it.isEmpty() }
        println("Filtered list\n${filteredList.joinToString(separator = "\n")}\n-----")
        val sortedList = filteredList.sortedWith { l, r ->
            val left = Json.parseToJsonElement(l).jsonArray
            val right = Json.parseToJsonElement(r).jsonArray
            when (compareJsonArrays(left, right)) {
                ItemOrdering.Correct -> -1
                ItemOrdering.Incorrect -> 1
                ItemOrdering.Undecided -> 0
            }
        }
        println("Sorted list:\n${sortedList.joinToString(separator = "\n")}")

        val start = sortedList.indexOf("[[2]]")+1
        val end = sortedList.indexOf("[[6]]")+1
        return start * end
    }

    val testInput = readInput("Day13_test")
    assertThat(part1(testInput)).isEqualTo(13)
    val input = readInput("Day13")
    println(part1(input))

    assertThat(part2(testInput)).isEqualTo(140)
    println(part2(input))
}