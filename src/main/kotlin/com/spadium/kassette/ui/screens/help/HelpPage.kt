package com.spadium.kassette.ui.screens.help

import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class HelpPage: Screen {
    private val parent: Screen?

    constructor(parent: Screen?, contents: Any?, title: Component): super(title) {
        this.parent = parent
    }

    override fun init() {
        super.init()
    }

    override fun onClose() {
        minecraft?.setScreen(parent)
    }
}