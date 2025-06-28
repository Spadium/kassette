package com.spadium.kassette.ui

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class ConfigScreen : Screen {
    val parent: Screen

    constructor(parent: Screen) : super(Text.translatable("kassette.options.title")) {
        this.parent = parent
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.render(context, mouseX, mouseY, deltaTicks)

    }

    override fun close() {
        this.client!!.setScreen(this.parent)
    }
}