package ru.alex.ss12.game.ai

import ru.alex.ss12.game.model.MoveDirection
import ru.alex.ss12.game.model.World
import ru.alex.ss12.model.User
import java.awt.Point
import java.util.LinkedList


abstract class Bot(
    name: String,
    val world: World,
    x: Int = 1,
    y: Int = 1,
    val type: BotType,
    val visibleRadius: Int
) : Character(name, x, y) {

    val seenBefore: MutableSet<Point> = mutableSetOf()
    var pointOfInterest: Point? = null
    val lookingFor: LookingFor = LookingFor()

    abstract fun makeMove(): BotMove

    data class BotMove(val direction: MoveDirection, val action: BotAction)

    enum class BotAction {
        None,

        //Означает, что бот только что обнаружил игрока
        FoundPlayer
    }

    enum class BotType {
        Enemy
    }

    fun pathToVisible(finder: (Point) -> Boolean) = pathFromMe(finder, visibleRadius)

    fun pathFromMe(finder: (Point) -> Boolean, depth: Int = Int.MAX_VALUE) = path(Point(x, y), finder, depth)

    fun path(from: Point, finder: (Point) -> Boolean, depth: Int = Int.MAX_VALUE): List<Point> {
        val visited: MutableSet<Point> = mutableSetOf()

        val nextToVisit: LinkedList<PathPart> = LinkedList<PathPart>()
        nextToVisit.add(PathPart(from, distance = 0))
        while (!nextToVisit.isEmpty()) {
            val current: PathPart = nextToVisit.remove()

            if (visited.contains(current.point))
                continue

            if (world.cellOn(current.point) == World.Cell.WALL) {
                visited.add(current.point)
                continue
            }

            if (finder(current.point)) {
                return backtrackPath(current)
            }

            if (current.distance + 1 < depth) {
                nextToVisit.add(PathPart(Point(current.point.x + 1, current.point.y), current, current.distance + 1))
                nextToVisit.add(PathPart(Point(current.point.x - 1, current.point.y), current, current.distance + 1))
                nextToVisit.add(PathPart(Point(current.point.x, current.point.y + 1), current, current.distance + 1))
                nextToVisit.add(PathPart(Point(current.point.x, current.point.y - 1), current, current.distance + 1))
            }
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
        return path.map { it.point }.reversed()
    }

    fun moveToPoint(point: Point, action: BotAction): BotMove {
        if (point.x < x)
            return BotMove(MoveDirection.LEFT, action)
        if (point.x > x)
            return BotMove(MoveDirection.RIGHT, action)
        if (point.y < y)
            return BotMove(MoveDirection.UP, action)
        if (point.y > y)
            return BotMove(MoveDirection.DOWN, action)
        return BotMove(MoveDirection.UNKNOWN, BotAction.None)
    }

    private data class PathPart(val point: Point, val parent: PathPart? = null, val distance: Int)

    inner class LookingFor {

        fun human(point: Point): Boolean {
            seenBefore.add(point)
            val isPlayer = world.cellOn(point) == World.Cell.HUMAN
            if (isPlayer)
                pointOfInterest = point
            return isPlayer
        }

        fun new(point: Point): Boolean {
            return !seenBefore.contains(point)
        }

        fun pointOfInterest(point: Point) : Boolean {
            return point == pointOfInterest
        }

    }

}