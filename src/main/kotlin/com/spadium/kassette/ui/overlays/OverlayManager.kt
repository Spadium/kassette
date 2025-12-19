package com.spadium.kassette.ui.overlays

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.Identifier
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object OverlayManager {
    private val MEDIA_LAYER: Identifier = Identifier.fromNamespaceAndPath("kassette", "media-layer")

    var renderOverlay: Boolean = true
    var currentOverlay: OverlayTheme = DefaultOverlay()
    val overlays: MutableMap<Identifier, KClass<out OverlayTheme>> = mutableMapOf(
        Identifier.parse("kassette:default") to DefaultOverlay::class,
        Identifier.parse("kassette:emtree") to EmTreeOverlay::class
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

    fun onRender(context: GuiGraphics, tickCounter: DeltaTracker) {
        // render when we are in the world, helps with the super-duper sped up spinning text on world load
        if (Minecraft.getInstance().gameRenderer.mainCamera.isInitialized && renderOverlay) {
            currentOverlay.render(context, tickCounter)
        }
    }
}