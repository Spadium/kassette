package com.spadium.kassette.ui.widgets

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ElementListWidget
import net.minecraft.client.gui.widget.LayoutWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import kotlin.properties.Delegates

class LayoutListWidget : ElementListWidget<LayoutListWidget.LayoutElement> {
    val layout: LayoutWidget

    constructor(client: MinecraftClient?, layout: LayoutWidget, parent: Screen, parentLayout: ThreePartsLayoutWidget) :
            super(client, parent.width, parentLayout.contentHeight, parentLayout.headerHeight, layout.height) {
        addEntry(LayoutElement(layout))
        this.layout = layout
    }

    companion object {
        fun <T: LayoutWidget> of(layout: T, client: MinecraftClient?, parent: Screen, parentLayout: ThreePartsLayoutWidget): Pair<T, LayoutListWidget> {
            val list: LayoutListWidget = LayoutListWidget(client, layout, parent, parentLayout)
            return Pair(layout, list)
        }
    }

    override fun getRowWidth(): Int {
        return layout.width.coerceAtMost(width)
    }

    inner class LayoutElement : Entry<LayoutElement> {
        val layout: LayoutWidget

        constructor(layout: LayoutWidget) {
            this.layout = layout
        }

        override fun render(
            context: DrawContext?,
            index: Int,
            y: Int,
            x: Int,
            entryWidth: Int,
            entryHeight: Int,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean,
            tickProgress: Float
        ) {
            layout.setPosition(x, y)
            layout.forEachChild {
                it.render(context, mouseX, mouseY, tickProgress)
            }
        }

        override fun children(): List<Element?>? {
            val children: MutableList<Element> = mutableListOf()
            layout.forEachChild { children.add(it) }
            return children
        }

        override fun selectableChildren(): List<Selectable?>? {
            val children: MutableList<Selectable> = mutableListOf()
            layout.forEachChild { if (it is Selectable) children.add(it) }
            return children
        }
    }
}