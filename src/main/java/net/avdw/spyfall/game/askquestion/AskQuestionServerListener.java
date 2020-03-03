package net.avdw.spyfall.game.askquestion;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.avdw.spyfall.network.NetworkGamePacketMapper;
import net.avdw.spyfall.network.ConnectionToPlayerMap;
import org.tinylog.Logger;

@Singleton
public class AskQuestionServerListener extends Listener {
    private final Server server;
    private final NetworkGamePacketMapper networkGamePacketMapper;
    private final AskQuestionAction askQuestionAction;

    @Inject
    public AskQuestionServerListener(final Server server,
                                     final NetworkGamePacketMapper networkGamePacketMapper,
                                     final AskQuestionAction askQuestionAction) {
        this.server = server;
        this.networkGamePacketMapper = networkGamePacketMapper;
        this.askQuestionAction = askQuestionAction;
    }

    @Override
    public void received(final Connection connection, final Object object) {
        if (object instanceof AskQuestionNetworkRequest) {
            AskQuestionNetworkRequest askQuestionNetworkRequest = (AskQuestionNetworkRequest) object;

            AskQuestionResponse askResponse = askQuestionAction.call(networkGamePacketMapper.mapToGameAskQuestionRequest(askQuestionNetworkRequest));
            server.sendToAllTCP(networkGamePacketMapper.mapToNetworkAskResponse(askResponse));

        }
    }
}
