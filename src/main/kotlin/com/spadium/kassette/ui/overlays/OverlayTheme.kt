package com.spadium.kassette.ui.overlays

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.util.Identifier

abstract class OverlayTheme {

    constructor() {

    }

    abstract fun render(context: DrawContext, tickCounter: RenderTickCounter)
}