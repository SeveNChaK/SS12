package ru.alex.ss12.game.ai

import ru.alex.ss12.game.model.MoveDirection
import ru.alex.ss12.game.model.World
import ru.alex.ss12.model.User
import java.awt.Point

class Terminator(
    world: World,
    x: Int = 1,
    y: Int = 1
) : Bot("T-800", world, x, y, BotType.Enemy, 6) {

    var haveLostPlayer: Boolean = true

    override fun makeMove(): BotMove {
        val pathToHuman = pathToVisible(lookingFor::human)
        if (pathToHuman.isNotEmpty() && pathToHuman.size > 1) {
            if (haveLostPlayer) {
                haveLostPlayer = false
                return moveToPoint(pathToHuman[1], BotAction.FoundPlayer)
            } else {
                return moveToPoint(pathToHuman[1], BotAction.None)
            }
        }

        haveLostPlayer = true

        if (pointOfInterest != null && x == pointOfInterest!!.x && y == pointOfInterest!!.y) {
            pointOfInterest == null;
        } else if (pointOfInterest != null) {
            val pathToPointOfInterest = pathFromMe(lookingFor::pointOfInterest)
            if (pathToPointOfInterest.isNotEmpty() && pathToPointOfInterest.size > 1) {
                return moveToPoint(pathToPointOfInterest[1], BotAction.None)
            }
        }

        val pathToNew = pathFromMe(lookingFor::new)
        if (pathToNew.isNotEmpty() && pathToNew.size > 1) {
            return moveToPoint(pathToNew[1], BotAction.None)
        }

        return BotMove(MoveDirection.UNKNOWN, BotAction.None)
    }

}