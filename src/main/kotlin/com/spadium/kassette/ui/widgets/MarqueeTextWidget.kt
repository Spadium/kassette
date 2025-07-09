package com.spadium.kassette.ui.widgets

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.text.Text

class MarqueeTextWidget: TextWidget {
    constructor(
        message: Text, textRenderer: TextRenderer
    ) : super(message, textRenderer) {

    }
}