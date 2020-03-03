package net.avdw.spyfall.game.readyplayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.avdw.spyfall.network.ConnectionToPlayerMap;
import net.avdw.spyfall.network.NetworkGamePacketMapper;
import org.tinylog.Logger;

@Singleton
public class ReadyPlayerServerListener extends Listener {
    private final Server server;
    private final NetworkGamePacketMapper networkGamePacketMapper;
    private final ReadyPlayerAction readyPlayerAction;

    @Inject
    ReadyPlayerServerListener(final Server server,
                              final NetworkGamePacketMapper networkGamePacketMapper,
                              final ReadyPlayerAction readyPlayerAction) {
        this.server = server;
        this.networkGamePacketMapper = networkGamePacketMapper;
        this.readyPlayerAction = readyPlayerAction;
    }

    @Override
    public void received(final Connection connection, final Object object) {
        if (object instanceof ReadyPlayerNetworkRequest) {
            ReadyPlayerNetworkRequest readyPlayerNetworkRequest = (ReadyPlayerNetworkRequest) object;
            ReadyPlayerResponse readyPlayerResponse = readyPlayerAction.call(networkGamePacketMapper.mapToGameReadyRequest(connection, readyPlayerNetworkRequest));
            server.sendToAllTCP(networkGamePacketMapper.mapToNetworkReadyResponse(readyPlayerResponse));
        }
    }
}
