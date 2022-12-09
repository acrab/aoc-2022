import com.google.common.truth.Truth.assertThat
import java.awt.Point


fun main() {

    fun moveToHead(head: Point, tail: Point) {
        //move tail if required
        if (head.x == tail.x) {
            //currently in the same row
            if (tail.y == head.y - 2) {
                tail.y += 1
            } else if (tail.y == head.y + 2) {
                tail.y -= 1
            }
        } else if (head.y == tail.y) {
            //currently in the same column, but not the same row
            if (tail.x == head.x - 2) {
                tail.x += 1
            } else if (tail.x == head.x + 2) {
                tail.x -= 1
            }
        } else {
            //not on same row or column, diagonal move may be needed
            if (tail.x == head.x - 2) {
                tail.x += 1
                if (tail.y < head.y) {
                    tail.y += 1
                } else {
                    tail.y -= 1
                }
            } else if (tail.x == head.x + 2) {
                tail.x -= 1
                if (tail.y < head.y) {
                    tail.y += 1
                } else {
                    tail.y -= 1
                }
            } else if (tail.y == head.y - 2) {
                tail.y += 1
                if (tail.x < head.x) {
                    tail.x += 1
                } else {
                    tail.x -= 1
                }
            } else if (tail.y == head.y + 2) {
                tail.y -= 1
                if (tail.x < head.x) {
                    tail.x += 1
                } else {
                    tail.x -= 1
                }
            }
        }
    }

    fun moveRope(input: List<String>, knotCount: Int, debug: Boolean): Int {
        //Map of visited coordinates
        val output = mutableMapOf(0 to mutableSetOf(0))
        val head = Point(0, 0)
        val knots = List(knotCount) { Point(0, 0)}
        val tail = knots.last()
        input.forEach {
            val (direction, distance) = it.split(" ")
            val deltaX = when (direction) {
                "L" -> -1
                "R" -> 1
                else -> 0
            }
            val deltaY = when (direction) {
                "U" -> 1
                "D" -> -1
                else -> 0
            }
            repeat(distance.toInt()) {
                //move head
                head.translate(deltaX, deltaY)
                //move tail if required
                knots.forEachIndexed { index, knot ->
                    if (debug) {
                        println("Moving knot $index")
                    }
                    if (index == 0) {
                        moveToHead(head, knot)
                    } else {
                        moveToHead(knots[index - 1], knot)
                    }
                }

                /*
                if (headX == tailX) {
                    //currently in the same row
                    if (tailY == headY - 2) {
                        tailY += 1
                    } else if (tailY == headY + 2) {
                        tailY -= 1
                    }
                } else if (headY == tailY) {
                    //currently in the same column, but not the same row
                    if (tailX == headX - 2) {
                        tailX += 1
                    } else if (tailX == headX + 2) {
                        tailX -= 1
                    }
                } else {
                    //not on same row or column, diagonal move may be needed
                    if (tailX == headX - 2) {
                        tailX += 1
                        if (tailY < headY) {
                            tailY += 1
                        } else {
                            tailY -= 1
                        }
                    } else if (tailX == headX + 2) {
                        tailX -= 1
                        if (tailY < headY) {
                            tailY += 1
                        } else {
                            tailY -= 1
                        }
                    } else if (tailY == headY - 2) {
                        tailY += 1
                        if (tailX < headX) {
                            tailX += 1
                        } else {
                            tailX -= 1
                        }
                    } else if (tailY == headY + 2) {
                        tailY -= 1
                        if (tailX < headX) {
                            tailX += 1
                        } else {
                            tailX -= 1
                        }
                    }
                }
                */
                if (debug) {
                    println("New positions: Head(${head.x}, ${head.y}), Tail(${tail.x}, ${tail.y})")
                }
                //mark new tail location
                if (output.containsKey(tail.x)) {
                    output[tail.x]!!.add(tail.y)
                } else {
                    output[tail.x] = mutableSetOf(tail.y)
                }
            }
        }
        return output.values.sumOf { it.size }
    }

    fun part1(input: List<String>, debug: Boolean): Int {
        return moveRope(input, 1, debug)
    }

    fun part2(input: List<String>, debug: Boolean): Int {
        return moveRope(input, 9, debug)
    }

    val testInput = readInput("Day09_test")
    assertThat(part1(testInput, true)).isEqualTo(13)
    val input = readInput("Day09")
    println(part1(input, false))

    val largerTestInput = readInput("Day09_largerTest")

    assertThat(part2(testInput, true)).isEqualTo(1)
    assertThat(part2(largerTestInput, true)).isEqualTo(36)
    println(part2(input, false))
}