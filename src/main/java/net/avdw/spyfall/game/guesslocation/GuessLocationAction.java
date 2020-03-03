package net.avdw.spyfall.game.guesslocation;

import com.google.inject.Inject;
import net.avdw.repository.Repository;
import net.avdw.spyfall.game.GamePhase;
import net.avdw.spyfall.game.memory.SpyfallPlayer;
import net.avdw.spyfall.game.Action;
import net.avdw.spyfall.game.memory.GameMemory;
import net.avdw.spyfall.game.memory.SpyfallPlayerByIdSpecification;

import java.util.List;

public class GuessLocationAction implements Action<GuessLocationRequest, GuessLocationResponse> {
    private final Repository<SpyfallPlayer> spyfallPlayerRepository;
    private final GameMemory gameMemory;

    @Inject
    GuessLocationAction(final Repository<SpyfallPlayer> spyfallPlayerRepository, final GameMemory gameMemory) {
        this.spyfallPlayerRepository = spyfallPlayerRepository;
        this.gameMemory = gameMemory;
    }

    @Override
    public GuessLocationResponse call(final GuessLocationRequest request) {
        List<SpyfallPlayer> spyfallPlayerByIdList = spyfallPlayerRepository.query(new SpyfallPlayerByIdSpecification(request.playerId));
        if (spyfallPlayerByIdList.size() != 1) {
            throw new UnsupportedOperationException();
        }
        SpyfallPlayer spyfallPlayer = spyfallPlayerByIdList.get(0);
        if (!gameMemory.gamePhase.equals(GamePhase.GUESSING_LOCATION)) {
            return new GuessLocationResponse(String.format("%s cannot guess location until they call for a guess!", spyfallPlayer));
        }

        gameMemory.gamePhase = GamePhase.SCORING;
        return new GuessLocationResponse();
    }
}
