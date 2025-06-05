package com.spadium.kassette.ui

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
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

    override fun close() {
        this.client!!.setScreen(this.parent!!)
    }
}