package cn.stars.reversal.command.impl;

import cn.stars.reversal.Reversal;
import cn.stars.reversal.command.Command;
import cn.stars.reversal.command.CommandManager;
import cn.stars.reversal.command.api.CommandInfo;
import net.minecraft.util.EnumChatFormatting;

@CommandInfo(name = "Help", description = "Sends all of the commands that currently exists in chat", syntax = ".help", aliases = "help")
public final class Help extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        Reversal.showMsg(EnumChatFormatting.WHITE + "All available commands:");

        for (final Command cmd : CommandManager.commandList) {
            final String description = cmd.getCommandInfo().description();
            final String alias = cmd.getCommandInfo().aliases()[0];

            if (!alias.contains("help")) {
                Reversal.showMsg(alias + ": " + description);
            }
        }
    }
}