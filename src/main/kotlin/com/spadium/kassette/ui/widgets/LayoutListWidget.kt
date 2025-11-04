package com.spadium.kassette.ui.widgets

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.screens.Screen
import kotlin.properties.Delegates

// i love mojmap
class LayoutListWidget : ContainerObjectSelectionList<LayoutListWidget.LayoutElement> {
    val layout: Layout

    constructor(client: Minecraft?, layout: Layout, parent: Screen, parentLayout: HeaderAndFooterLayout) :
            super(client, parent.width, parentLayout.contentHeight, parentLayout.headerHeight, layout.height) {
        addEntry(LayoutElement(layout))
        this.layout = layout
    }

    companion object {
        fun <T: Layout> of(layout: T, client: Minecraft?, parent: Screen, parentLayout: HeaderAndFooterLayout): Pair<T, LayoutListWidget> {
            val list: LayoutListWidget = LayoutListWidget(client, layout, parent, parentLayout)
            return Pair(layout, list)
        }
    }

    override fun getRowWidth(): Int {
        return layout.width.coerceAtMost(width)
    }

    inner class LayoutElement : Entry<LayoutElement> {
        val layout: Layout

        constructor(layout: Layout) {
            this.layout = layout
        }

        override fun render(
            context: GuiGraphics,
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
            layout.visitWidgets {
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