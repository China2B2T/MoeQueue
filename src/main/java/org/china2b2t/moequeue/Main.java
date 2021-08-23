/*
 * Copyright (c) 2021 China2B2T All rights reserved
 * And coded by the lovely bunny we love~
 */

package org.china2b2t.moequeue;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.china2b2t.moequeue.commands.CommandLogin;
import org.china2b2t.moequeue.commands.CommandRegister;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Main extends Plugin {
    public static Main instance = null;
    public static Configuration cfg = null;
    public static DbService db = null;

    @Override
    public void onEnable() {
        // getLogger().log(Level.INFO, "MoeQueue is now loading~");
        instance = this;
        this.getProxy().getPluginManager().registerListener(this, new MListener());
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File configPath = Main.instance.getDataFolder();
        try {
            cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(configPath, "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CommandLogin());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CommandRegister());

        try {
            MongoClient client = null;

            if (cfg.getBoolean("database.auth")) {
                ServerAddress serverAddress = new ServerAddress(cfg.getString("database.address"), cfg.getInt("database.port"));

                MongoCredential credential = MongoCredential.createScramSha1Credential(
                        cfg.getString("database.auth-info.username"),
                        cfg.getString("database.database"),
                        cfg.getString("database.auth-info.password")
                                .toCharArray()
                );
                List<MongoCredential> credentials = new ArrayList<MongoCredential>();
                credentials.add(credential);

                client = new MongoClient(serverAddress, credentials);
            } else {
                client = new MongoClient(cfg.getString("database.address"), cfg.getInt("database.port"));
            }

            Main.db = new DbService(client);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Invalid database configuration: " + e.getMessage());
        }

        getProxy().getScheduler().schedule(this, new QueueTask(), 1, cfg.getInt("interval"), TimeUnit.SECONDS);
    }

    public void onDisable() {
        db.disconnect();
    }

    public static void reloadConfig() throws IOException {
        File configPath = Main.instance.getDataFolder();
        cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(configPath, "config.yml"));
    }
}
