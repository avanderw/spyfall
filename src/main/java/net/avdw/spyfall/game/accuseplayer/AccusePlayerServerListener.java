package net.avdw.spyfall.game.accuseplayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.avdw.repository.Repository;
import net.avdw.spyfall.network.NetworkGamePacketMapper;
import net.avdw.spyfall.game.memory.SpyfallPlayer;
import net.avdw.spyfall.game.memory.SpyfallPlayerByIdSpecification;
import net.avdw.spyfall.network.ConnectionToPlayerMap;
import org.tinylog.Logger;

import java.util.List;

@Singleton
public class AccusePlayerServerListener extends Listener {
    private final ConnectionToPlayerMap connectionToPlayerMap;
    private final NetworkGamePacketMapper networkGamePacketMapper;
    private final Repository<SpyfallPlayer> spyfallPlayerRepository;
    private final AccusePlayerAction accusePlayerAction;

    @Inject
    public AccusePlayerServerListener(final ConnectionToPlayerMap connectionToPlayerMap,
                                      final NetworkGamePacketMapper networkGamePacketMapper,
                                      final Repository<SpyfallPlayer> spyfallPlayerRepository,
                                      final AccusePlayerAction accusePlayerAction) {
        this.connectionToPlayerMap = connectionToPlayerMap;
        this.networkGamePacketMapper = networkGamePacketMapper;
        this.spyfallPlayerRepository = spyfallPlayerRepository;
        this.accusePlayerAction = accusePlayerAction;
    }

    @Override
    public void received(final Connection connection, final Object object) {
        if (object instanceof AccusePlayerNetworkRequest) {
            AccusePlayerNetworkRequest accusePlayerNetworkRequest = (AccusePlayerNetworkRequest) object;

            SpyfallPlayer accusingPlayer = connectionToPlayerMap.get(connection);
            List<SpyfallPlayer> spyfallPlayerList = spyfallPlayerRepository.query(new SpyfallPlayerByIdSpecification(accusePlayerNetworkRequest.getAccusedId()));
            if (spyfallPlayerList.isEmpty()) {
                throw new UnsupportedOperationException();
            }
            SpyfallPlayer accusedPlayer = spyfallPlayerList.get(0);

            Logger.info("{} accused {} of being the spy", accusingPlayer.getName(), accusedPlayer.getName());
            AccusePlayerResponse accusePlayerResponse = accusePlayerAction.call(networkGamePacketMapper.mapToGameAccusePlayerRequest(accusePlayerNetworkRequest));
            networkGamePacketMapper.mapToNetworkAccuseResponse(accusePlayerResponse);
        }
    }
}
