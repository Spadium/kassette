package com.spadium.kassette.ui.overlays

import com.spadium.kassette.media.MediaProvider
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.util.Identifier
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object OverlayManager {
    private val MEDIA_LAYER: Identifier = Identifier.of("kassette", "media-layer")

    var currentOverlay: OverlayTheme = DefaultOverlay()
    val overlays: MutableMap<Identifier, KClass<out OverlayTheme>> = mutableMapOf(
        Identifier.of("kassette:default") to DefaultOverlay::class,
        Identifier.of("kassette:emtree") to EmTreeOverlay::class
    ).withDefault { DefaultOverlay::class }

    init {
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.HOTBAR, MEDIA_LAYER,
            this::onRender
        )
    }

    fun registerOverlay(identifier: Identifier, overlay: OverlayTheme) {
        overlays.put(identifier, overlay::class)
    }

    fun setOverlay(identifier: Identifier) {
        currentOverlay = overlays[identifier]?.createInstance() ?: DefaultOverlay()
    }

    fun onRender(context: DrawContext, tickCounter: RenderTickCounter) {
        currentOverlay.render(context, tickCounter)
    }
}