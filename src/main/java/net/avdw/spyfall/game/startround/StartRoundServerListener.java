package net.avdw.spyfall.game.startround;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.avdw.spyfall.network.NetworkGamePacketMapper;

@Singleton
public class StartRoundServerListener extends Listener {
    private final NetworkGamePacketMapper networkGamePacketMapper;
    private final StartRoundAction startRoundAction;
    private final Server server;

    @Inject
    public StartRoundServerListener(final NetworkGamePacketMapper networkGamePacketMapper,
                                    final StartRoundAction startRoundAction,
                                    final Server server) {
        this.networkGamePacketMapper = networkGamePacketMapper;
        this.startRoundAction = startRoundAction;
        this.server = server;
    }

    @Override
    public void received(final Connection connection, final Object object) {
        if (object instanceof StartRoundNetworkRequest) {
            StartRoundNetworkRequest startRoundNetworkRequest = (StartRoundNetworkRequest) object;
            StartRoundResponse startRoundResponse = startRoundAction.call(networkGamePacketMapper.toGameStartRoundRequest(startRoundNetworkRequest));
            server.sendToAllTCP(networkGamePacketMapper.mapToNetworkStartRoundResponse(startRoundResponse));
        }
    }
}
