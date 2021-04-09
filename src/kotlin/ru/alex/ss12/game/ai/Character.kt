package ru.alex.ss12.game.ai

import ru.alex.ss12.game.model.MoveDirection

open class Character(val name: String, var x: Int = 1, var y: Int = 1) {

    @Transient
    private var isMoveEnabled = true

    fun move(direction: MoveDirection) {
        when (direction) {
            MoveDirection.UP -> y -= 1
            MoveDirection.DOWN -> y += 1
            MoveDirection.RIGHT -> x += 1
            MoveDirection.LEFT -> x -= 1
            MoveDirection.UNKNOWN -> {
                return
            } //TODO
        }
        disableMove()
    }

    fun enableMove() {
        isMoveEnabled = true
    }

    fun disableMove() {
        isMoveEnabled = false
    }

    fun isMoveEnabled() = isMoveEnabled

}