package com.spadium.kassette.config

import com.spadium.kassette.ui.ConfigScreen
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.minecraft.client.gui.screen.Screen

class ModMenuImpl : ModMenuApi, ConfigScreenFactory<ConfigScreen> {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*>? {

        return super.getModConfigScreenFactory()
    }

    override fun create(parent: Screen?): ConfigScreen? {
        TODO("Not yet implemented")
    }
}