package net.avdw.spyfall.game.startround;

import com.google.inject.Inject;
import net.avdw.repository.Repository;
import net.avdw.spyfall.game.Action;
import net.avdw.spyfall.game.GamePhase;
import net.avdw.spyfall.game.memory.SpyfallPlayer;
import net.avdw.spyfall.game.memory.GameMemory;
import net.avdw.spyfall.game.memory.SpyfallPlayerSpecification;

import java.util.List;
import java.util.Random;

public class StartRoundAction implements Action<StartRoundRequest, StartRoundResponse> {
    private final Repository<SpyfallPlayer> spyfallPlayerRepository;
    private final GameMemory gameMemory;
    private final Random random;

    @Inject
    StartRoundAction(Repository<SpyfallPlayer> spyfallPlayerRepository, final GameMemory gameMemory, final Random random) {
        this.spyfallPlayerRepository = spyfallPlayerRepository;
        this.gameMemory = gameMemory;
        this.random = random;
    }
    @Override
    public StartRoundResponse call(final StartRoundRequest request) {
        List<SpyfallPlayer> spyfallPlayerList = spyfallPlayerRepository.query(new SpyfallPlayerSpecification());
        if (spyfallPlayerList.stream().anyMatch(SpyfallPlayer::isNotReady)) {
            return new StartRoundResponse("Cannot start round until all players are ready!");
        }
        if (spyfallPlayerList.size() <= 2) {
            return new StartRoundResponse("Cannot start round until you have more than three players!");
        }

        if (gameMemory.previousStartingPlayer == null) {
            gameMemory.currentPlayer = spyfallPlayerList.stream().skip(random.nextInt(spyfallPlayerList.size())).findFirst().orElseThrow(UnsupportedOperationException::new);
            gameMemory.previousStartingPlayer = gameMemory.currentPlayer;
        } else {
            gameMemory.currentPlayer = gameMemory.previousSpyPlayer;
        }

        SpyfallPlayer spyPlayer = spyfallPlayerList.stream().skip(random.nextInt(spyfallPlayerList.size())).findAny().orElseThrow(UnsupportedOperationException::new);
        spyPlayer.makeSpy();
        gameMemory.gamePhase = GamePhase.ROUND_PLAYING;
        return new StartRoundResponse(gameMemory.currentPlayer, spyPlayer);
    }
}
