package com.spadium.kassette.ui

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class ConfigScreen : Screen {
    val parent: Screen?

    constructor(title: Text, parent: Screen) : super(title) {
        this.parent = parent
    }

    constructor(title: Text) : super(title) {
        this.parent = null
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.render(context, mouseX, mouseY, deltaTicks)

    }

    override fun close() {
        this.client!!.setScreen(this.parent!!)
    }
}