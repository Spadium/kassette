package com.spadium.kassette.ui.screens.config

import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class HelpScreen: Screen {
    val parent: Screen?

    constructor(parent: Screen?) : super(Text.translatable("kassette.help.title")) {
        this.parent = parent
    }

    override fun close() {
        client?.setScreen(parent)
    }
}