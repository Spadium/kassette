package com.spadium.kassette.ui.overlays

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object OverlayManager {
    private val MEDIA_LAYER: ResourceLocation = ResourceLocation.fromNamespaceAndPath("kassette", "media-layer")

    var currentOverlay: OverlayTheme = DefaultOverlay()
    val overlays: MutableMap<ResourceLocation, KClass<out OverlayTheme>> = mutableMapOf(
        ResourceLocation.parse("kassette:default") to DefaultOverlay::class,
        ResourceLocation.parse("kassette:emtree") to EmTreeOverlay::class
    ).withDefault { DefaultOverlay::class }

    init {
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.HOTBAR, MEDIA_LAYER,
            this::onRender
        )
    }

    fun registerOverlay(identifier: ResourceLocation, overlay: OverlayTheme) {
        overlays.put(identifier, overlay::class)
    }

    fun setOverlay(identifier: ResourceLocation) {
        currentOverlay = overlays[identifier]?.createInstance() ?: DefaultOverlay()
    }

    fun onRender(context: GuiGraphics, tickCounter: DeltaTracker) {
        // render when we are in the world, helps with the super-duper sped up spinning text on world load
        if (Minecraft.getInstance().gameRenderer.mainCamera.isInitialized) {
            currentOverlay.render(context, tickCounter)
        }
    }
}