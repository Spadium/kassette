package com.spadium.kassette.ui.config

import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class AboutScreen: Screen {
    private val parent: Screen?

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.providers.title")) {
        this.parent = parent
    }

    override fun close() {
        client!!.setScreen(parent)
    }
}