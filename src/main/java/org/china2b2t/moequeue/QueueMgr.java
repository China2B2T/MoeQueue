/*
 * Copyright (c) 2021 China2B2T All rights reserved
 * And coded by the lovely bunny we love~
 */

package org.china2b2t.moequeue;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Random;

public class QueueMgr {
    private static int defSum = 0;
    private static int priorSum = 0;

    private static HashMap<ProxiedPlayer, Integer> defQ = new HashMap<ProxiedPlayer, Integer>();
    private static HashMap<ProxiedPlayer, Integer> priorQ = new HashMap<ProxiedPlayer, Integer>();

    public static void addDef(ProxiedPlayer player) {
        var add = new Random().nextInt(abs(Main.cfg.getInt("fake-players.default.max") - Main.cfg.getInt("fake-players.default.min"))) + Main.cfg.getInt("fake-players.default.min");
        defSum += add + 1;
        defQ.put(player, add);
    }

    public static void addPrior(ProxiedPlayer player) {
        var add = new Random().nextInt(abs(Main.cfg.getInt("fake-players.prior.max") - Main.cfg.getInt("fake-players.prior.min"))) + Main.cfg.getInt("fake-players.prior.min");
        priorSum += add + 1;
        priorQ.put(player, add);
    }

    private static int abs(int i) {
        if(i <= 0) {
            return -i;
        }
        return i;
    }
}
