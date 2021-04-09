package ru.alex.ss12.model

import ru.alex.ss12.game.ai.Character
import ru.alex.ss12.game.model.Item

class User(
    name: String,
    x: Int = 1,
    y: Int = 1,
    val items: MutableList<Item.Type> = mutableListOf()
) : Character(name, x, y) {

    fun addItem(item: Item) {
        items.add(item.type)
    }

    fun removeItem(typeItem: Item.Type) {
        items.remove(typeItem)
    }

}
