package net.avdw.spyfall.network.server.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.avdw.spyfall.game.SpyfallGame;
import net.avdw.spyfall.game.SpyfallPlayer;
import net.avdw.spyfall.network.server.ConnectionSpyfallPlayerMap;
import org.pmw.tinylog.Logger;

import java.util.UUID;

@Singleton
public class AddPlayerListener extends Listener {
    private final SpyfallGame spyfallGame;
    private final ConnectionSpyfallPlayerMap connectionSpyfallPlayerMap;

    @Inject
    AddPlayerListener(final SpyfallGame spyfallGame, final ConnectionSpyfallPlayerMap connectionSpyfallPlayerMap) {
        this.spyfallGame = spyfallGame;
        this.connectionSpyfallPlayerMap = connectionSpyfallPlayerMap;
    }

    @Override
    public void connected(final Connection connection) {
        SpyfallPlayer spyfallPlayer = new SpyfallPlayer(UUID.randomUUID().toString());
        spyfallGame.addPlayer(spyfallPlayer);
        connectionSpyfallPlayerMap.map(connection, spyfallPlayer);
        Logger.debug("{} connected and assigned player {}", connection, spyfallPlayer);
    }
}
