import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.math.absoluteValue

class Day12 : Day<Int, Int> {

    companion object {
        private const val NORTH = 'N'
        private const val SOUTH = 'S'
        private const val EAST = 'E'
        private const val WEST = 'W'
        private const val LEFT = 'L'
        private const val RIGHT = 'R'
        private const val FORWARD = 'F'
    }

    override val name: String
        get() = "Day 12"

    private val input = File("src/main/resources/day12.txt").readLines()

    interface Command {
        val value: Int
        fun execute(shipPosition: ShipPosition): ShipPosition
    }

    object Q1Command {
        class North(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                return shipPosition.copy(vertical = shipPosition.vertical - value)
            }
        }

        class East(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                return shipPosition.copy(horizontal = shipPosition.horizontal - value)
            }
        }

        class South(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                return shipPosition.copy(vertical = shipPosition.vertical + value)
            }
        }

        class West(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                return shipPosition.copy(horizontal = shipPosition.horizontal + value)
            }
        }

        class Left(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                val angle = Math.floorMod(shipPosition.direction.angle - value, 360)
                return shipPosition.copy(direction = Direction.fromAngle(angle))
            }
        }

        class Right(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                val angle = Math.floorMod(shipPosition.direction.angle + value, 360)
                return shipPosition.copy(direction = Direction.fromAngle(angle))
            }
        }

        class Forward(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                return when (shipPosition.direction) {
                    Direction.NORTH -> shipPosition.copy(vertical = shipPosition.vertical - value)
                    Direction.EAST -> shipPosition.copy(horizontal = shipPosition.horizontal - value)
                    Direction.SOUTH -> shipPosition.copy(vertical = shipPosition.vertical + value)
                    Direction.WEST -> shipPosition.copy(horizontal = shipPosition.horizontal + value)
                }
            }
        }
    }

    object Q2Command {
        class North(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                return shipPosition.copy(wayPointVertical = shipPosition.wayPointVertical - value)
            }
        }

        class East(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                return shipPosition.copy(wayPointHorizontal = shipPosition.wayPointHorizontal - value)
            }
        }

        class South(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                return shipPosition.copy(wayPointVertical = shipPosition.wayPointVertical + value)
            }
        }

        class West(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                return shipPosition.copy(wayPointHorizontal = shipPosition.wayPointHorizontal + value)
            }
        }

        class Left(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                val angle = Math.floorMod(shipPosition.direction.angle + value, 360)
                return when (value / 90) {
                    1 -> shipPosition.copy(
                        wayPointHorizontal = shipPosition.wayPointVertical * -1,
                        wayPointVertical = shipPosition.wayPointHorizontal,
                        direction = Direction.fromAngle(angle)
                    )
                    2 -> shipPosition.copy(
                        wayPointHorizontal = shipPosition.wayPointHorizontal * -1,
                        wayPointVertical = shipPosition.wayPointVertical * -1,
                        direction = Direction.fromAngle(angle)
                    )
                    3 -> shipPosition.copy(
                        wayPointHorizontal = shipPosition.wayPointVertical,
                        wayPointVertical = shipPosition.wayPointHorizontal * -1,
                        direction = Direction.fromAngle(angle)
                    )
                    else -> throw IllegalStateException("Don't handle $value")
                }
            }
        }

        class Right(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                val angle = Math.floorMod(shipPosition.direction.angle - value, 360)
                return when (value / 90) {
                    1 -> shipPosition.copy(
                        wayPointHorizontal = shipPosition.wayPointVertical,
                        wayPointVertical = shipPosition.wayPointHorizontal * -1,
                        direction = Direction.fromAngle(angle)
                    )
                    2 -> shipPosition.copy(
                        wayPointHorizontal = shipPosition.wayPointHorizontal * -1,
                        wayPointVertical = shipPosition.wayPointVertical * -1,
                        direction = Direction.fromAngle(angle)
                    )
                    3 -> shipPosition.copy(
                        wayPointHorizontal = shipPosition.wayPointVertical * -1,
                        wayPointVertical = shipPosition.wayPointHorizontal,
                        direction = Direction.fromAngle(angle)
                    )
                    else -> throw IllegalStateException("Don't handle $value")
                }
            }
        }

        class Forward(override val value: Int) : Command {
            override fun execute(shipPosition: ShipPosition): ShipPosition {
                return shipPosition.copy(
                    horizontal = shipPosition.horizontal + value * shipPosition.wayPointHorizontal,
                    vertical = shipPosition.vertical + value * shipPosition.wayPointVertical
                )
            }
        }
    }

    private fun String.toQ1Command(): Command {
        val value = substring(1 until length).toInt()
        return when (get(0)) {
            NORTH -> Q1Command.North(value)
            SOUTH -> Q1Command.South(value)
            EAST -> Q1Command.East(value)
            WEST -> Q1Command.West(value)
            LEFT -> Q1Command.Left(value)
            RIGHT -> Q1Command.Right(value)
            FORWARD -> Q1Command.Forward(value)
            else -> throw IllegalArgumentException("Command ${get(0)} not found")
        }
    }

    private fun String.toQ2Command(): Command {
        val value = substring(1 until length).toInt()
        return when (get(0)) {
            NORTH -> Q2Command.North(value)
            SOUTH -> Q2Command.South(value)
            EAST -> Q2Command.East(value)
            WEST -> Q2Command.West(value)
            LEFT ->Q2Command.Left(value)
            RIGHT -> Q2Command.Right(value)
            FORWARD -> Q2Command.Forward(value)
            else -> throw IllegalArgumentException("Command ${get(0)} not found")
        }
    }

    enum class Direction(val angle: Int) {
        NORTH(0), EAST(90), SOUTH(180), WEST(270);

        companion object {
            fun fromAngle(angle: Int): Direction {
                return values().first { it.angle == angle }
            }
        }
    }

    data class ShipPosition(
        val wayPointVertical: Int,
        val wayPointHorizontal: Int,
        val vertical: Int, val horizontal: Int, val direction: Direction
    )

    override fun answerQuestion1(): Int {
        val shipPosition = input
            .map { it.toQ1Command() }
            .fold(ShipPosition(-1, -10, 0, 0, Direction.EAST)) { shipPosition, command ->
                command.execute(shipPosition).also { println(it) }
            }

        return shipPosition.vertical.absoluteValue + shipPosition.horizontal.absoluteValue
    }

    override fun answerQuestion2(): Int {
        val shipPosition = input
            .map { it.toQ2Command() }
            .fold(ShipPosition(-1, -10, 0, 0, Direction.EAST)) { shipPosition, command ->
                command.execute(shipPosition).also { println(it) }
            }

        return shipPosition.vertical.absoluteValue + shipPosition.horizontal.absoluteValue
    }


}