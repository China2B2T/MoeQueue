/*
 * Copyright (c) 2021 China2B2T All rights reserved
 * And coded by the lovely bunny we love~
 */

package org.china2b2t.moequeue;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class QueueMgr {
    private static int premiumSum = 0;
    private static int priorSum = 0;
    private static int offlineSum = 0;

    private static HashMap<ProxiedPlayer, Integer> offlineQ = new HashMap<>();
    private static HashMap<ProxiedPlayer, Integer> premiumQ = new HashMap<>();
    private static HashMap<ProxiedPlayer, Integer> priorQ = new HashMap<>();

    public static void addPremium(ProxiedPlayer player) {
        if (Main.instance.getProxy().getServers().containsKey(Main.cfg.getString("target"))) {
            if (
                    Main.instance.getProxy().
                            getServerInfo(Main.cfg.getString("target")).
                            getPlayers().
                            size() >= Main.cfg.getInt("max-players")
            ) {
                link(player);
            }
        }

        int add = new Random().nextInt(abs(Main.cfg.getInt("fake-players.premium.max") - Main.cfg.getInt("fake-players.premium.min"))) + Main.cfg.getInt("fake-players.premium.min");
        premiumSum += add + 1;
        premiumQ.put(player, add);
    }

    public static void addOffline(ProxiedPlayer player) {
        if (Main.instance.getProxy().getServers().containsKey(Main.cfg.getString("target"))) {
            if (
                    Main.instance.getProxy().
                            getServerInfo(Main.cfg.getString("target")).
                            getPlayers().
                            size() >= Main.cfg.getInt("max-players")
            ) {
                link(player);
            }
        }

        int add = new Random().nextInt(abs(Main.cfg.getInt("fake-players.offline.max") - Main.cfg.getInt("fake-players.prior.min"))) + Main.cfg.getInt("fake-players.offline.min");
        offlineSum += add + 1;
        offlineQ.put(player, add);
    }

    public static void addPrior(ProxiedPlayer player) {
        if (Main.instance.getProxy().getServers().containsKey(Main.cfg.getString("target"))) {
            if (
                    Main.instance.getProxy().
                            getServerInfo(Main.cfg.getString("target")).
                            getPlayers().
                            size() >= Main.cfg.getInt("max-players")
            ) {
                link(player);
            }
        }

        int add = new Random().nextInt(abs(Main.cfg.getInt("fake-players.prior.max") - Main.cfg.getInt("fake-players.prior.min"))) + Main.cfg.getInt("fake-players.prior.min");
        priorSum += add + 1;
        priorQ.put(player, add);
    }

    public static void nextPeriod() {
        Set<ProxiedPlayer> premiumSet = premiumQ.keySet();
        Set<ProxiedPlayer> priorSet = priorQ.keySet();
        Set<ProxiedPlayer> offlineSet = offlineQ.keySet();

        if (Main.instance.getProxy().getServers().containsKey(Main.cfg.getString("target"))) {
            if (
                    Main.instance.getProxy().
                            getServerInfo(Main.cfg.getString("target")).
                            getPlayers().
                            size() >= Main.cfg.getInt("max-players")
            ) {
                return;
            }
        }

        // Prior loop
        for (ProxiedPlayer i : priorSet) {
            int position = priorQ.get(i);
            position--;
            if (position <= 0) {
                priorQ.remove(i);
                link(i);
                // i.sendMessage(ChatMessageType.CHAT, new TextComponent("You finished the queue!"));
                continue;
            }
            priorQ.put(i, position);
            i.sendMessage(ChatMessageType.SYSTEM, new TextComponent(ChatColor.GOLD + "Position in queue: " + ChatColor.BOLD + position));
        }

        // Premium loop
        for (ProxiedPlayer i : premiumSet) {
            int position = premiumQ.get(i);
            position--;
            if (position <= 0) {
                premiumQ.remove(i);
                link(i);
                // i.sendMessage(ChatMessageType.CHAT, new TextComponent("You finished the queue!"));
                continue;
            }
            premiumQ.put(i, position);
            i.sendMessage(ChatMessageType.CHAT, new TextComponent(ChatColor.GOLD + "Position in queue: " + ChatColor.BOLD + position));
        }

        for (ProxiedPlayer i : offlineSet) {
            int position = offlineQ.get(i);
            position--;
            if (position <= 0) {
                offlineQ.remove(i);
                link(i);
                // i.sendMessage(ChatMessageType.CHAT, new TextComponent("You finished the queue!"));
                continue;
            }
            offlineQ.put(i, position);
            i.sendMessage(ChatMessageType.CHAT, new TextComponent(ChatColor.GOLD + "Position in queue: " + ChatColor.BOLD + position));
        }
    }

    private static int abs(int i) {
        return i <= 0 ? -i : i;
    }

    private static void link(ProxiedPlayer p) {
        if (!Main.instance.getProxy().getServers().containsKey(Main.cfg.getString("target"))) {
            throw new IllegalStateException("No target server found.");
        }

        p.connect(Main.instance.getProxy().getServerInfo(Main.cfg.getString("target")));
    }
}
