package net.avdw.spyfall.game.accuseplayer;

import com.google.inject.Inject;
import net.avdw.repository.Repository;
import net.avdw.spyfall.game.GamePhase;
import net.avdw.spyfall.game.memory.SpyfallPlayer;
import net.avdw.spyfall.game.Action;
import net.avdw.spyfall.game.memory.GameMemory;
import net.avdw.spyfall.game.memory.SpyfallPlayerByIdSpecification;

import java.util.List;

public class StartAccusePlayerAction implements Action<StartAccusePlayerRequest, StartAccusePlayerResponse> {
    private final Repository<SpyfallPlayer> spyfallPlayerRepository;
    private final GameMemory gameMemory;

    @Inject
    StartAccusePlayerAction(final Repository<SpyfallPlayer> spyfallPlayerRepository, final GameMemory gameMemory) {
        this.spyfallPlayerRepository = spyfallPlayerRepository;
        this.gameMemory = gameMemory;
    }
    @Override
    public StartAccusePlayerResponse call(final StartAccusePlayerRequest request) {
        List<SpyfallPlayer> spyfallPlayerByIdList = spyfallPlayerRepository.query(new SpyfallPlayerByIdSpecification(request.playerId));
        if (spyfallPlayerByIdList.size() != 1) {
            throw new UnsupportedOperationException();
        }
        SpyfallPlayer spyfallPlayer = spyfallPlayerByIdList.get(0);
        if (spyfallPlayer.hasAccusedSpy()) {
            return new StartAccusePlayerResponse(String.format("%s cannot accuse the spy more than once in a round!", spyfallPlayer));
        }
        if (gameMemory.gamePhase.equals(GamePhase.ACCUSING_PLAYER)) {
            return new StartAccusePlayerResponse(String.format("%s cannot call an accusation whilst everyone is accusing the spy!", spyfallPlayer));
        }

        gameMemory.gamePhase = GamePhase.ACCUSING_PLAYER;
        spyfallPlayer.callAccuseSpy();
        return new StartAccusePlayerResponse();
    }
}
