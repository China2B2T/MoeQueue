/*
 * Copyright (c) 2021 China2B2T All rights reserved
 * And coded by the lovely bunny we love~
 */

package org.china2b2t.moequeue;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;

public class MListener implements net.md_5.bungee.api.plugin.Listener {
    @EventHandler
    public void onPostLogin(PostLoginEvent e) throws IOException {
        var player = e.getPlayer();
        var playerName = player.getName();
        if(Main.cfg.get("prior-list." + playerName) != null && Main.cfg.getLong("prior-list." + playerName) >= System.currentTimeMillis()) {

        }
    }
}
