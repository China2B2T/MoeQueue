/*
 * Copyright (c) 2021 China2B2T All rights reserved
 * And coded by the lovely bunny we love~
 */

package org.china2b2t.moequeue;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;

import static org.china2b2t.moequeue.QueueMgr.addOffline;
import static org.china2b2t.moequeue.QueueMgr.addPremium;

public class MListener implements net.md_5.bungee.api.plugin.Listener {
    private static boolean containsHanScript(String s) {
        return s.codePoints().anyMatch(
                codepoint ->
                        Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }

    @EventHandler(priority = -10)
    public void onPostLogin(PostLoginEvent e) throws IOException {
        ProxiedPlayer player = e.getPlayer();

        String uuid = player.getUniqueId().toString();
//        if(Main.cfg.get("prior-list." + playerName) != null && Main.cfg.getLong("prior-list." + playerName) >= System.currentTimeMillis()) {
//            addPrior(player);
//            return;
//        }
        if (containsHanScript(player.getPendingConnection().getName())) {
            Main.db.data.put(player.getUniqueId().toString(), false);
            player.sendMessage(ChatMessageType.SYSTEM, new TextComponent(ChatColor.GOLD + "Login with /login <password> | Register with /register <password> <retype password>"));
            // addOffline(player);
        } else {
            Main.db.data.put(player.getUniqueId().toString(), true);
            // Register this player.
            if (!Main.db.hasRegistered(player.getUniqueId().toString())) {
                Main.db.register(player.getUniqueId().toString());
            }
            addPremium(player);
        }
    }
}
