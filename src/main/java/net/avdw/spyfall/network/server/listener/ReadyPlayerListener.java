package net.avdw.spyfall.network.server.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.avdw.spyfall.mapper.NetworkGamePacketMapper;
import net.avdw.spyfall.game.SpyfallGame;
import net.avdw.spyfall.game.packet.ReadyResponse;
import net.avdw.spyfall.network.packet.ReadyPlayerRequest;
import net.avdw.spyfall.network.server.ConnectionSpyfallPlayerMap;
import org.pmw.tinylog.Logger;

@Singleton
public class ReadyPlayerListener extends Listener {
    private final SpyfallGame spyfallGame;
    private final ConnectionSpyfallPlayerMap connectionSpyfallPlayerMap;
    private NetworkGamePacketMapper networkGamePacketMapper;

    @Inject
    ReadyPlayerListener(final SpyfallGame spyfallGame, final ConnectionSpyfallPlayerMap connectionSpyfallPlayerMap, final NetworkGamePacketMapper networkGamePacketMapper) {

        this.spyfallGame = spyfallGame;
        this.connectionSpyfallPlayerMap = connectionSpyfallPlayerMap;
        this.networkGamePacketMapper = networkGamePacketMapper;
    }
    @Override
    public void received(final Connection connection, final Object object) {
        if (object instanceof ReadyPlayerRequest) {
            Logger.debug("ReadyPlayerRequest");
            ReadyResponse readyResponse = spyfallGame.ready(connectionSpyfallPlayerMap.get(connection).getId());
            connection.sendTCP(networkGamePacketMapper.mapToNetworkReadyResponse(readyResponse));
        }
    }
}
