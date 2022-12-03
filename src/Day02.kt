import Opponent.Companion.toOpponent
import Player.Companion.toPlayer
import Result.Companion.toResult
import com.google.common.truth.Truth.assertThat

enum class Opponent(val key: String) {
    Rock("A"),
    Paper("B"),
    Scissors("C");

    companion object {
        fun String.toOpponent() = values().first { it.key == this }
    }
}

enum class Player(val key: String, val score: Int) {
    Rock("X", 1),
    Paper("Y", 2),
    Scissors("Z", 3);

    companion object {
        fun String.toPlayer() = values().first { it.key == this }
    }
}

enum class Result(val key: String) {
    Lose("X"),
    Draw("Y"),
    Win("Z");

    companion object {
        fun String.toResult() = values().first { it.key == this }
    }
}

fun main() {

    fun win(with: Player) = 6 + with.score
    fun draw(with: Player) = 3 + with.score
    fun lose(with: Player) = 0 + with.score

    fun pointsForRound(them: Opponent, us: Player): Int =
        when (us) {
            Player.Rock -> when (them) {
                Opponent.Rock -> draw(us)
                Opponent.Paper -> lose(us)
                Opponent.Scissors -> win(us)
            }

            Player.Paper -> when (them) {
                Opponent.Rock -> win(us)
                Opponent.Paper -> draw(us)
                Opponent.Scissors -> lose(us)
            }

            Player.Scissors -> when (them) {
                Opponent.Rock -> lose(us)
                Opponent.Paper -> win(us)
                Opponent.Scissors -> draw(us)
            }
        }

    fun symbolForWin(other: Opponent) =
        when (other) {
            Opponent.Rock -> Player.Paper
            Opponent.Paper -> Player.Scissors
            Opponent.Scissors -> Player.Rock
        }

    fun symbolForDraw(other: Opponent) =
        when (other) {
            Opponent.Rock -> Player.Rock
            Opponent.Paper -> Player.Paper
            Opponent.Scissors -> Player.Scissors
        }

    fun symbolForLoss(other: Opponent) =
        when (other) {
            Opponent.Rock -> Player.Scissors
            Opponent.Paper -> Player.Rock
            Opponent.Scissors -> Player.Paper
        }

    fun symbolForResult(other: Opponent, result: Result): Player =
        when (result) {
            Result.Lose -> symbolForLoss(other)
            Result.Draw -> symbolForDraw(other)
            Result.Win -> symbolForWin(other)
        }

    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .map { it.split(" ") }
            .sumOf { pointsForRound(it[0].toOpponent(), it[1].toPlayer()) }
    }

    fun part2(input: List<String>): Int {
        return input.asSequence()
            .map { it.split(" ") }
            .map { it[0].toOpponent() to it[1].toResult() }
            .sumOf { pointsForRound(it.first, symbolForResult(it.first, it.second)) }
    }

    val testInput = readInput("Day02_test")
    assertThat(part1(testInput)).isEqualTo(15)
    val input = readInput("Day02")
    println(part1(input))

    assertThat(part2(testInput)).isEqualTo(12)
    println(part2(input))
}