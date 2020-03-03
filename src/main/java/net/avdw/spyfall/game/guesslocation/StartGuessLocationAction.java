package net.avdw.spyfall.game.guesslocation;

import com.google.inject.Inject;
import net.avdw.repository.Repository;
import net.avdw.spyfall.game.GamePhase;
import net.avdw.spyfall.game.memory.SpyfallPlayer;
import net.avdw.spyfall.game.Action;
import net.avdw.spyfall.game.memory.GameMemory;
import net.avdw.spyfall.game.memory.SpyfallPlayerByIdSpecification;

import java.util.List;

public class StartGuessLocationAction implements Action<StartGuessLocationRequest, StartGuessLocationResponse> {
    private final Repository<SpyfallPlayer> spyfallPlayerRepository;
    private final GameMemory gameMemory;

    @Inject
    StartGuessLocationAction(Repository<SpyfallPlayer> spyfallPlayerRepository, final GameMemory gameMemory) {
        this.spyfallPlayerRepository = spyfallPlayerRepository;
        this.gameMemory = gameMemory;
    }
    @Override
    public StartGuessLocationResponse call(final StartGuessLocationRequest request) {
        List<SpyfallPlayer> spyfallPlayerByIdList = spyfallPlayerRepository.query(new SpyfallPlayerByIdSpecification(request.playerId));
        if (spyfallPlayerByIdList.size() != 1) {
            throw new UnsupportedOperationException();
        }
        SpyfallPlayer spyfallPlayer = spyfallPlayerByIdList.get(0);
        if (gameMemory.gamePhase.equals(GamePhase.GUESSING_LOCATION)) {
            return new StartGuessLocationResponse(String.format("%s cannot call a guess as a guess has already been called.", spyfallPlayer));
        }

        if (spyfallPlayer.isNotSpy()) {
            return new StartGuessLocationResponse(String.format("%s cannot call a guess as they are not the spy", spyfallPlayer));
        }

        gameMemory.gamePhase = GamePhase.GUESSING_LOCATION;
        return new StartGuessLocationResponse();
    }
}
