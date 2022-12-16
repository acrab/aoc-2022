import com.google.common.truth.Truth.assertThat
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs

data class Sensor(val sensorX: Int, val sensorY: Int, val beaconX: Int, val beaconY: Int)

fun String.toSensor(): Sensor {
    val parts = split(" ")
    return Sensor(
        sensorX = parts[2].removePrefix("x=").removeSuffix(",").toInt(),
        sensorY = parts[3].removePrefix("y=").removeSuffix(":").toInt(),
        beaconX = parts[8].removePrefix("x=").removeSuffix(",").toInt(),
        beaconY = parts[9].removePrefix("y=").toInt(),
    )
}

fun main() {
    fun calculateRangeOnRow(sensorX: Int, sensorY: Int, beaconX: Int, beaconY: Int, targetRow: Int): Pair<Int, Int>? {
        //Calculate manhattan distance to beacon
        val beaconDistance = abs(sensorX - beaconX) + abs(sensorY - beaconY)
        //calculate distance to target row
        val rowDistance = abs(sensorY - targetRow)
        //calculate range on that row
        return if (rowDistance > beaconDistance) {
            //There's no overlap between this sensor's swept area and our target row
            null
        } else {
            val d = beaconDistance - rowDistance
            sensorX - d to sensorX + d
        }
    }

    fun List<Pair<Int, Int>?>.consolidateRanges() = filterNotNull()
        .fold(emptyList<Pair<Int, Int>>()) { acc, pair ->

            if (acc.isEmpty()) {
                listOf(pair)
            } else {
                var (start, end) = pair

                acc.forEach { (s, e) ->
                    if (start in s..e) {
                        start = e
                    }
                    if (end in s..e) {
                        end = s
                    }
                }
                //we're covered by existing ranges
                if (end <= start) {
                    acc
                } else if (acc.any { it.first < start && it.second > end }) {
                    //we're completely covered by a single existing range
                    acc
                } else {
                    //remove any ranges we completely cover, and add this range
                    acc.filterNot { it.first > start && it.second < end } + (start to end)
                }
            }
        }

    fun List<Pair<Int, Int>?>.consolidateRangesBetween(minStart: Int, maxEnd: Int) = filterNotNull()
        .fold(emptyList<Pair<Int, Int>>()) { acc, pair ->

            if (acc.isEmpty()) {
                listOf(pair)
            } else {
                var (start, end) = pair
                start = max(start, minStart)
                end = min(end, maxEnd)
                acc.forEach { (s, e) ->
                    if (start in s..e) {
                        start = e
                    }
                    if (end in s..e) {
                        end = s
                    }
                }
                //we're covered by existing ranges
                if (end <= start) {
                    acc
                } else if (acc.any { it.first <= start && it.second >= end }) {
                    //we're completely covered by a single existing range
                    acc
                } else {
                    //remove any ranges we completely cover, and add this range
                    acc.filterNot { it.first >= start && it.second <= end } + (start to end)
                }
            }
        }

    fun part1(input: List<String>, targetRow: Int): Int {
        val ranges = input.map {
            val parts = it.split(" ")
            val sensorX = parts[2].removePrefix("x=").removeSuffix(",").toInt()
            val sensorY = parts[3].removePrefix("y=").removeSuffix(":").toInt()
            val beaconX = parts[8].removePrefix("x=").removeSuffix(",").toInt()
            val beaconY = parts[9].removePrefix("y=").toInt()

            calculateRangeOnRow(sensorX, sensorY, beaconX, beaconY, targetRow)
        }
        println("Ranges: $ranges")
        //consolidate overlapping ranges
        val consolidatedRanges = ranges.consolidateRanges()
        println("Ranges: $consolidatedRanges")
        //sum separate ranges

        return consolidatedRanges
            .sumOf { it.second - it.first }
    }

    fun frequency(x: Int, y: Int): Long = (x * 4_000_000L) + y

    fun part2(input: List<String>, maxSize: Int): Long {
        val sensors = input.map(String::toSensor)
        repeat(maxSize) { row ->
            val ranges = sensors.map { calculateRangeOnRow(it.sensorX, it.sensorY, it.beaconX, it.beaconY, row) }
                .consolidateRangesBetween(0, maxSize)
            ranges.forEach { range ->
                if (range.second < maxSize && ranges.none { it.first == range.second }) {
                    println("Calculating frequency for ${range.second + 1}, $row")
                    return frequency(range.second + 1, row)
                }
            }
        }
        return 0
    }

    val testInput = readInput("Day15_test")
    assertThat(part1(testInput, 10)).isEqualTo(26)
    val input = readInput("Day15")
    println(part1(input, 2_000_000))

    assertThat(part2(testInput, 20)).isEqualTo(56000011L)
    println(part2(input, 4_000_000))
}