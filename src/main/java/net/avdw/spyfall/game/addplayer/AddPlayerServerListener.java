package net.avdw.spyfall.game.addplayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.avdw.spyfall.network.NetworkGamePacketMapper;
import net.avdw.spyfall.network.ConnectionToPlayerMap;

@Singleton
public class AddPlayerServerListener extends Listener {
    private final Server server;
    private final ConnectionToPlayerMap connectionToPlayerMap;
    private final NetworkGamePacketMapper networkGamePacketMapper;
    private final AddPlayerAction addPlayerAction;

    @Inject
    AddPlayerServerListener(final Server server,
            final ConnectionToPlayerMap connectionToPlayerMap,
            final NetworkGamePacketMapper networkGamePacketMapper,
            final AddPlayerAction addPlayerAction) {
        this.server = server;
        this.connectionToPlayerMap = connectionToPlayerMap;
        this.networkGamePacketMapper = networkGamePacketMapper;
        this.addPlayerAction = addPlayerAction;
    }

    @Override
    public void received(final Connection connection, final Object object) {
        if (object instanceof AddPlayerNetworkRequest) {
            AddPlayerNetworkRequest addPlayerNetworkRequest = (AddPlayerNetworkRequest) object;
            AddPlayerRequest addPlayerRequest = networkGamePacketMapper.toGameAddPlayerRequest(connection, addPlayerNetworkRequest);
            AddPlayerResponse addPlayerResponse = addPlayerAction.call(addPlayerRequest);
            connectionToPlayerMap.map(connection, addPlayerResponse.spyfallPlayer);
            server.sendToAllTCP(networkGamePacketMapper.mapToNetworkAddPlayerResponse(addPlayerResponse));
        }
    }
}
