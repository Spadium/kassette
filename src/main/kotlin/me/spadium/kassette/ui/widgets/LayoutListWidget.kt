package me.spadium.kassette.ui.widgets

import me.spadium.kassette.Kassette
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.Screen
import kotlin.properties.Delegates

// i love mojmap
class LayoutListWidget : ContainerObjectSelectionList<LayoutListWidget.LayoutElement> {
    val layout: Layout

    constructor(client: Minecraft, layout: Layout, parent: Screen, parentLayout: HeaderAndFooterLayout) :
            super(client, parent.width, parentLayout.contentHeight, parentLayout.headerHeight, layout.height) {
        Kassette.logger.warn("LayoutListWidget(Minecraft, Layout, Screen, HeaderAndFooterLayout)")
        addEntry(LayoutElement(layout))
        this.layout = layout
    }

    companion object {
        fun <T: Layout> of(layout: T, client: Minecraft, parent: Screen, parentLayout: HeaderAndFooterLayout): Pair<T, LayoutListWidget> {
            val list: LayoutListWidget = LayoutListWidget(client, layout, parent, parentLayout)
            return Pair(layout, list)
        }
    }

    override fun getRowWidth(): Int {
        // this is null for some reason???????????????
        @Suppress("SENSELESS_COMPARISON")
        if (layout != null) {
            return layout.width.coerceAtMost(width)
        }
        return super.rowWidth
    }

    inner class LayoutElement : Entry<LayoutElement> {
        val layout: Layout

        constructor(layout: Layout) {
            this.layout = layout
        }

//        override fun render(
//            context: GuiGraphics,
//            index: Int,
//            y: Int,
//            x: Int,
//            entryWidth: Int,
//            entryHeight: Int,
//            mouseX: Int,
//            mouseY: Int,
//            hovered: Boolean,
//            tickProgress: Float
//        ) {
//            layout.setPosition(x, y)
//            layout.visitWidgets {
//                it.render(context, mouseX, mouseY, tickProgress)
//            }
//        }

        override fun children(): List<GuiEventListener> {
            val children: MutableList<GuiEventListener> = mutableListOf()

            layout.visitWidgets {
                children.add(it)
            }

            return children
        }

        override fun narratables(): List<NarratableEntry> {
            val children: MutableList<NarratableEntry> = mutableListOf()
            layout.visitChildren { if (it is NarratableEntry) children.add(it) }
            return children
        }

        override fun renderContent(
            guiGraphics: GuiGraphics,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean,
            a: Float
        ) {
            layout.setPosition(x, y)
            layout.visitWidgets {
                it.render(
                    guiGraphics, mouseX, mouseY, a
                )
            }
        }
    }
}