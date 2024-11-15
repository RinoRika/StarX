package cn.stars.reversal.command.impl;

import cn.stars.reversal.RainyAPI;
import cn.stars.reversal.Reversal;
import cn.stars.reversal.command.Command;
import cn.stars.reversal.command.api.CommandInfo;
import cn.stars.reversal.config.ConfigHandler;
import cn.stars.reversal.ui.notification.NotificationType;

@CommandInfo(name = "Config", description = "Modify your configs", syntax = ".config <save/create/load/list/delete> <name>", aliases = {"config", "cfg"})
public final class Config extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        RainyAPI.executorService.execute(() -> {
            switch (args[0].toLowerCase()) {
                case "save": {
                    if (args[1].isEmpty()) {
                        Reversal.showMsg("Invalid config name.");
                        Reversal.showMsg(".config save <name>");
                        Reversal.notificationManager.registerNotification("Invalid config name.", "Command", NotificationType.ERROR);
                        return;
                    }
                    ConfigHandler.save(args[1]);
                    break;
                }

                case "create": {
                    if (args[1].isEmpty()) {
                        Reversal.showMsg("Invalid config name.");
                        Reversal.showMsg(".config create <name>");
                        Reversal.notificationManager.registerNotification("Invalid config name.", "Command", NotificationType.ERROR);
                        return;
                    }
                    ConfigHandler.create(args[1]);
                    break;
                }

                case "load": {
                    ConfigHandler.load(args[1]);
                    break;
                }

                case "list": {
                    ConfigHandler.list();
                    break;
                }

                case "delete": {
                    ConfigHandler.delete(args[1]);
                    break;
                }

                default: {
                    Reversal.showMsg(".config <save/create/load/list/delete> <name>");
                    Reversal.notificationManager.registerNotification("Invalid usage of command.", "Command", NotificationType.ERROR);
                }
            }
        });
    }
}