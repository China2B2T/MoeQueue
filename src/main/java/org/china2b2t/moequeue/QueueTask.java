/*
 * Copyright (c) 2021 China2B2T All rights reserved
 * And coded by the lovely bunny we love~
 */

package org.china2b2t.moequeue;

public class QueueTask implements Runnable {
    @Override
    public void run() {
        QueueMgr.nextPeriod();
    }
}
