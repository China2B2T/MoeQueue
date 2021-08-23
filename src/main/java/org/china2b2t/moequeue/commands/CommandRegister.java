package org.china2b2t.moequeue.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.protocol.packet.Chat;
import org.china2b2t.moequeue.Main;

public class CommandRegister extends Command {
    public CommandRegister() {
        super("register");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Requires player."));
        }

        assert sender instanceof ProxiedPlayer;
        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (Main.db.hasLoggedIn(player.getUniqueId().toString())) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "You are already logged in."));
            return;
        }

        if (Main.db.hasRegistered(player.getUniqueId().toString())) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "An account exists, please login throught /login <password>."));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /register <password> <retype password>"));
            return;
        }

        if (!args[0].equals(args[1])) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "The password didn't match the retyped password."));
        } else {
            Main.db.register(player.getUniqueId().toString(), args[1]);
            player.sendMessage(ChatMessageType.SYSTEM, new TextComponent(ChatColor.GOLD + "OK! Now login with /login <password>."));
        }
    }
}
