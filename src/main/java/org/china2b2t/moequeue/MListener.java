/*
 * Copyright (c) 2021 China2B2T All rights reserved
 * And coded by the lovely bunny we love~
 */

package org.china2b2t.moequeue;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;

import static org.china2b2t.moequeue.QueueMgr.addOffline;
import static org.china2b2t.moequeue.QueueMgr.addPremium;

public class MListener implements net.md_5.bungee.api.plugin.Listener {
    @EventHandler(priority = -10)
    public void onPostLogin(PostLoginEvent e) throws IOException {
        ProxiedPlayer player = e.getPlayer();

        String uuid = player.getUniqueId().toString();
//        if(Main.cfg.get("prior-list." + playerName) != null && Main.cfg.getLong("prior-list." + playerName) >= System.currentTimeMillis()) {
//            addPrior(player);
//            return;
//        }
        if (!player.getPendingConnection().isOnlineMode()) {
            addOffline(player);
        } else {
            addPremium(player);
        }
    }
}
