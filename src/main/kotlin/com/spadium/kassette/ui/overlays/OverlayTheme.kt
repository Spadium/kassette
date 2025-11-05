package com.spadium.kassette.ui.overlays

import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics

abstract class OverlayTheme {

    constructor() {

    }

    abstract fun render(context: GuiGraphics, tickCounter: DeltaTracker)
}