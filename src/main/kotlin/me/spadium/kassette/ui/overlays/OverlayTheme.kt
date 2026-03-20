package me.spadium.kassette.ui.overlays

import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphicsExtractor

abstract class OverlayTheme {

    constructor() {

    }

    abstract fun render(context: GuiGraphicsExtractor, tickCounter: DeltaTracker)
}