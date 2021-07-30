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
    private static int defSum = 0;
    private static int priorSum = 0;

    private static HashMap<ProxiedPlayer, Integer> defQ = new HashMap<ProxiedPlayer, Integer> ( );
    private static HashMap<ProxiedPlayer, Integer> priorQ = new HashMap<ProxiedPlayer, Integer> ( );

    public static void addDef(ProxiedPlayer player) {
        int add = new Random ( ).nextInt ( abs ( Main.cfg.getInt ( "fake-players.default.max" ) - Main.cfg.getInt ( "fake-players.default.min" ) ) ) + Main.cfg.getInt ( "fake-players.default.min" );
        defSum += add + 1;
        defQ.put ( player, add );
    }

    public static void addPrior(ProxiedPlayer player) {
        int add = new Random ( ).nextInt ( abs ( Main.cfg.getInt ( "fake-players.prior.max" ) - Main.cfg.getInt ( "fake-players.prior.min" ) ) ) + Main.cfg.getInt ( "fake-players.prior.min" );
        priorSum += add + 1;
        priorQ.put ( player, add );
    }

    public static void nextPeriod() {
        Set<ProxiedPlayer> defSet = defQ.keySet ( );
        Set<ProxiedPlayer> priorSet = priorQ.keySet ( );

        if (defSet.isEmpty ( )) {
            return;
        }

        if (Main.instance.getProxy ( ).getServers ( ).containsKey ( Main.cfg.getString ( "target" ) )) {
            if (
                    Main.instance.getProxy ( ).
                            getServerInfo ( Main.cfg.getString ( "target" ) ).
                            getPlayers ( ).
                            size ( ) >= Main.cfg.getInt ( "max-players" )
            ) {
                return;
            }
        }

        for (ProxiedPlayer i : defSet) {
            int position = defQ.get ( i );
            position--;
            if (position <= 0) {
                defQ.remove ( i );
                // link(i);
                i.sendMessage ( ChatMessageType.CHAT, new TextComponent ( "You finished the queue!" ) );
                continue;
            }
            defQ.put ( i, position );
            i.sendMessage ( ChatMessageType.CHAT, new TextComponent ( ChatColor.GOLD + "Position in queue: " + ChatColor.BOLD + position ) );
        }

        if (priorSet.isEmpty ( )) {
            return;
        }

        for (ProxiedPlayer i : priorSet) {
            int position = priorQ.get ( i );
            position--;
            if (position <= 0) {
                defQ.remove ( i );
                // link(i);
                i.sendMessage ( ChatMessageType.CHAT, new TextComponent ( "You finished the queue!" ) );
                continue;
            }
            defQ.put ( i, position );
            i.sendMessage ( ChatMessageType.SYSTEM, new TextComponent ( ChatColor.GOLD + "Position in queue: " + ChatColor.BOLD + position ) );
        }
    }

    private static int abs(int i) {
        return i <= 0 ? -i : i;
    }

    private static void link(ProxiedPlayer p) {
        if (!Main.instance.getProxy ( ).getServers ( ).containsKey ( Main.cfg.getString ( "target" ) )) {
            throw new IllegalStateException ( "No target server found." );
        }

        p.connect ( Main.instance.getProxy ( ).getServerInfo ( Main.cfg.getString ( "target" ) ) );
    }
}
