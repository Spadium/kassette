package me.spadium.kassette.ui.screens.config

import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class HelpScreen: Screen {
    val parent: Screen?

    constructor(parent: Screen?) : super(Component.translatable("kassette.help.title")) {
        this.parent = parent
    }

    override fun onClose() {
        minecraft?.setScreen(parent)
    }
}