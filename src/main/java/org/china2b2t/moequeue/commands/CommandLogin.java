package org.china2b2t.moequeue.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.china2b2t.moequeue.Main;
import org.china2b2t.moequeue.QueueMgr;

public class CommandLogin extends Command {
    public CommandLogin() {
        super("login");
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
        }

        if (args.length != 1) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /login <password>"));
        }

        if (Main.db.login(player.getUniqueId().toString(), args[0])) {
            Main.db.data.put(player.getUniqueId().toString(), true);
            QueueMgr.addOffline(player);
        } else {
            player.getPendingConnection().disconnect(new TextComponent(ChatColor.GOLD + "Incorrect password!"));
        }
    }
}
