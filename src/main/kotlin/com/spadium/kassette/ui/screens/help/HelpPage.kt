package com.spadium.kassette.ui.screens.help

import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class HelpPage: Screen {
    private val parent: Screen?

    constructor(parent: Screen?, contents: Any?, title: Text): super(title) {
        this.parent = parent
    }

    override fun init() {
        super.init()
    }

    override fun close() {
        client?.setScreen(parent)
    }
}