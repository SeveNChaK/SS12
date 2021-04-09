package ru.alex.ss12.game.ai

import ru.alex.ss12.game.model.MoveDirection
import ru.alex.ss12.game.model.World
import ru.alex.ss12.model.User
import java.awt.Point
import java.util.LinkedList


abstract class Bot(
    name: String,
    val world: World,
    val players: List<User>,
    x: Int = 1,
    y: Int = 1,
    val type: BotType
) : Character(name, x, y) {

    abstract fun makeMove() : BotMove

    data class BotMove(val direction: MoveDirection, val action: BotAction)

    enum class BotAction {
        None,
        //Означает, что бот только что обнаружил игрока
        FoundPlayer
    }

    enum class BotType {
        Enemy
    }

    fun path(from: Point, to: Point): List<Point> {
        val visited: MutableSet<Point> = mutableSetOf()

        val nextToVisit: LinkedList<PathPart> = LinkedList<PathPart>()
        nextToVisit.add(PathPart(from))
        while (!nextToVisit.isEmpty()) {
            val current: PathPart = nextToVisit.remove()

            if (visited.contains(current.point))
                continue

            if (world.cellOn(current.point) == World.Cell.WALL) {
                visited.add(current.point)
                continue
            }

            if (current.point == to) {
                return backtrackPath(current)
            }

            nextToVisit.add(PathPart(Point(current.point.x + 1, current.point.y), current))
            nextToVisit.add(PathPart(Point(current.point.x - 1, current.point.y), current))
            nextToVisit.add(PathPart(Point(current.point.x, current.point.y + 1), current))
            nextToVisit.add(PathPart(Point(current.point.x, current.point.y - 1), current))
            visited.add(current.point)

        }
        return emptyList()
    }

    private fun backtrackPath(from: PathPart): List<Point> {
        val path: MutableList<PathPart> = mutableListOf()
        var current: PathPart? = from
        while (current != null) {
            path.add(current)
            current = current.parent
        }
        return path.map{ it.point }
    }

    private data class PathPart(val point: Point, val parent: PathPart? = null)

}