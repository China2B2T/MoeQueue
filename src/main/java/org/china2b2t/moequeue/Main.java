/*
 * Copyright (c) 2021 China2B2T All rights reserved
 * And coded by the lovely bunny we love~
 */

package org.china2b2t.moequeue;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class Main extends Plugin {
    public static Main instance = null;
    public static Configuration cfg = null;

    @Override
    public void onEnable() {
        getLogger().info("MoeQueue is now loading~");
        instance = this;
        this.getProxy().getPluginManager().registerListener(this, new MListener());
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        var file = new File(getDataFolder(), "config.yml");


        if (!file.exists()) {
            try (var in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        var configPath = Main.instance.getDataFolder();
        try {
            cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(configPath, "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        getProxy().getScheduler().schedule(this, new QueueTask(), 0, 1, TimeUnit.SECONDS);
    }

    public static void reloadConfig() throws IOException {
        var configPath = Main.instance.getDataFolder();
        cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(configPath, "config.yml"));
    }
}
