package net.avdw.spyfall.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.google.inject.Singleton;
import net.avdw.spyfall.game.SpyfallPlayer;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class ConnectionSpyfallPlayerMap {
    private final Map<Connection, SpyfallPlayer> connectionSpyfallPlayerMap = new HashMap<>();

    public void map(final Connection connection, final SpyfallPlayer spyfallPlayer) {
        connectionSpyfallPlayerMap.put(connection, spyfallPlayer);
    }

    public SpyfallPlayer get(final Connection connection) {
        return connectionSpyfallPlayerMap.get(connection);
    }
}
