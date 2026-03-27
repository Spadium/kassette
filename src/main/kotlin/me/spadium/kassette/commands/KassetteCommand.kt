package me.spadium.kassette.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import me.spadium.kassette.config.Config
import me.spadium.kassette.media.MediaManager
import me.spadium.kassette.ui.screens.config.ConfigScreen
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.ClientCommands
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.Minecraft
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.arguments.IdentifierArgument
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

class KassetteCommand : ClientCommandRegistrationCallback {
    val client = Minecraft.getInstance()

    class KassetteReloadCommand : Command<FabricClientCommandSource> {
        override fun run(ctx: CommandContext<FabricClientCommandSource?>?): Int {
            Config.reloadAll()
            return 1
        }

    }

    inner class KassetteConfigCommand : Command<FabricClientCommandSource> {
        override fun run(ctx: CommandContext<FabricClientCommandSource?>?): Int {
            client.execute {
                client.setScreen(ConfigScreen(null))
            }
            return 1
        }
    }

    inner class KassetteProviderCommand : Command<FabricClientCommandSource> {
        override fun run(ctx: CommandContext<FabricClientCommandSource>?): Int {
            val arg = ctx?.getArgument("identifier", Identifier::class.java)
            if (arg == null) {
                ctx?.source?.sendError(Component.literal("no identifier!"))
                return 0
            }
            MediaManager.setProvider(arg)
            ctx.source?.sendFeedback(Component.literal("Set media provider to: ${arg.toString()}"))
            return 1
        }
    }

    override fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        buildContext: CommandBuildContext
    ) {
        dispatcher.register(
            ClientCommands.literal("kassette")
                .then(ClientCommands.literal("reload").executes(KassetteReloadCommand()))
                .then(ClientCommands.literal("config").executes(KassetteConfigCommand()))
        )
    }
}