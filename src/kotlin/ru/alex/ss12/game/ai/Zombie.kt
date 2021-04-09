package ru.alex.ss12.game.ai

import ru.alex.ss12.game.model.MoveDirection
import ru.alex.ss12.game.model.World
import ru.alex.ss12.model.User

class Zombie(world: World, players: List<User>, x: Int = 1, y: Int = 1) :
    Bot("Зомби", world, players, x, y, BotType.Enemy) {

    override fun makeMove(): BotMove {
        return BotMove(
            MoveDirection.values().random(),
            BotAction.None
        )
    }

}