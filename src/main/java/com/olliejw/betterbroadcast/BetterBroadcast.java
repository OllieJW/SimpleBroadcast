package com.olliejw.betterbroadcast;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterBroadcast extends JavaPlugin {
    public final Logger logger = Logger.getLogger("Minecraft");

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args) {
        // Broadcast
        if (cmd.getName().equalsIgnoreCase("broadcast")) {
            if (args.length == 0) {
                // No arguments error
                String Error = getConfig().getString("ErrorMessage");
                assert Error != null;
                Error = ChatColor.translateAlternateColorCodes('&', Error);
                sender.sendMessage(Error);
            }
            // Get message
            else {
                String Prefix = "";
                String Suffix = "";

                boolean ShowPrefix = getConfig().getBoolean("ShowPrefix");
                boolean ShowSuffix = getConfig().getBoolean("ShowSuffix");

                for (String arg : args) {
                    if (arg.contains("p:")) {
                        Prefix = arg;
                    }
                    // Checks if our 1st arg is not a prefix
                    else if (!(args[0]).contains("p:")) {
                        if (ShowPrefix) {
                            Prefix = getConfig().getString("Prefix");
                        }
                    }

                    if (arg.contains("s:")) {
                        Suffix = arg;
                    }
                    // Checks if our last arg is a not a suffix
                    else if (!(args[args.length-1]).contains("s:")) {
                        if (ShowSuffix) {
                            Suffix = getConfig().getString("Suffix");
                        }
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }

                // We remove our Prefix/Suffix from this to avoid duplication as "Content" will contain the p: and s: stuff
                assert Prefix != null;
                assert Suffix != null;
                String Content = sb.toString()
                     .replace(Prefix, "")
                     .replace(Suffix, "");
                String Broadcast = (Prefix+Content+Suffix)
                     .replace("p:", "")
                     .replace("s:", "");
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Broadcast));

            }

            // Looks like we want to reload our config
            if (cmd.getName().equalsIgnoreCase("bbreload")) {
                // Is the user allowed to do this?
                if (sender.hasPermission(Objects.requireNonNull(getConfig().getString("ReloadPerm"))))
                    // Phew. Looks like they are. We will try reload the config
                    try {
                        // It worked! Reload the config
                        reloadConfig();
                        saveDefaultConfig();
                        // Just let 'em know we have done the job
                        String Reload = getConfig().getString("ReloadMessage");
                        assert Reload != null;
                        Reload = ChatColor.translateAlternateColorCodes('&', Reload);
                        sender.sendMessage(Reload);
                    } catch (Exception e) {
                        // Uh oh. Something has gone wrong. Lets tell the sender
                        sender.sendMessage(ChatColor.RED + "Failed to reload. Check syntax");
                    }
            }
            return true;
        }
        return true;
    }
}

