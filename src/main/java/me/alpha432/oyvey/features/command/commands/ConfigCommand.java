/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.alpha432.oyvey.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;

public class ConfigCommand
extends Command {
    public ConfigCommand() {
        super("config", new String[]{"<save/load>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            ConfigCommand.sendMessage("You`ll find the config files in your gameProfile directory under Xuluplus/config");
            return;
        }
        if (commands.length == 2) {
            if ("list".equals(commands[0])) {
                String configs = "Configs: ";
                File file = new File("Xuluplus/");
                List directories = Arrays.stream(file.listFiles()).filter(File::isDirectory).filter(f -> !f.getName().equals("util")).collect(Collectors.toList());
                StringBuilder builder = new StringBuilder(configs);
                for (File file1 : directories) {
                    builder.append(file1.getName() + ", ");
                }
                configs = builder.toString();
                ConfigCommand.sendMessage(configs);
            } else {
                ConfigCommand.sendMessage("Not a valid command... Possible usage: <list>");
            }
        }
        if (commands.length >= 3) {
            switch (commands[0]) {
                case "save": {
                    OyVey.configManager.saveConfig(commands[1]);
                    ConfigCommand.sendMessage((Object)ChatFormatting.GREEN + "Config '" + commands[1] + "' has been saved.");
                    return;
                }
                case "load": {
                    if (OyVey.configManager.configExists(commands[1])) {
                        OyVey.configManager.loadConfig(commands[1]);
                        ConfigCommand.sendMessage((Object)ChatFormatting.GREEN + "Config '" + commands[1] + "' has been loaded.");
                    } else {
                        ConfigCommand.sendMessage((Object)ChatFormatting.RED + "Config '" + commands[1] + "' does not exist.");
                    }
                    return;
                }
            }
            ConfigCommand.sendMessage("Not a valid command... Possible usage: <save/load>");
        }
    }
}

