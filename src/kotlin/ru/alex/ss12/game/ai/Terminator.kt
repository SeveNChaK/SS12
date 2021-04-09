package ru.alex.ss12.game.ai

import ru.alex.ss12.game.model.World
import ru.alex.ss12.model.User

class Terminator(
    world: World,
    players: List<User>,
    x: Int = 1,
    y: Int = 1
) : Bot("T-800", world, players, x, y, BotType.Enemy) {

    override fun makeMove(): BotMove {
        world.cellOn(x, y)
        throw NotImplementedError("TODO");
    }

}