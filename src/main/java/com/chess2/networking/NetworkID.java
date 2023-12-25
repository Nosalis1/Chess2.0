package com.chess2.networking;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class NetworkID {
    private static final Set<Long> allIds;

    static {
        allIds = new HashSet<>();
        allIds.add(Packet.SERVER_SENDER_ID);
    }

    public static long getId() {
        Random random = new Random();
        long id;
        do {
            id = 1000 + Math.abs(random.nextLong() % (9999 - 1000 + 1));
        } while (!allIds.add(id));
        return id;
    }

    public static void freeId(final long id) {
        if (id == Packet.SERVER_SENDER_ID) return;
        allIds.remove(id);
    }
}
