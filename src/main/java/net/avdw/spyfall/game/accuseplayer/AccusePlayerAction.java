package net.avdw.spyfall.game.accuseplayer;

import com.google.inject.Inject;
import net.avdw.repository.Repository;
import net.avdw.spyfall.game.GamePhase;
import net.avdw.spyfall.game.memory.SpyfallPlayer;
import net.avdw.spyfall.game.Action;
import net.avdw.spyfall.game.memory.GameMemory;
import net.avdw.spyfall.game.memory.SpyfallPlayerByIdSpecification;
import net.avdw.spyfall.game.memory.SpyfallPlayerSpecification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccusePlayerAction implements Action<AccusePlayerRequest, AccusePlayerResponse> {
    private final Repository<SpyfallPlayer> spyfallPlayerRepository;
    private final GameMemory gameMemory;

    @Inject
    AccusePlayerAction(final Repository<SpyfallPlayer> spyfallPlayerRepository, final GameMemory gameMemory) {
        this.spyfallPlayerRepository = spyfallPlayerRepository;
        this.gameMemory = gameMemory;
    }

    @Override
    public AccusePlayerResponse call(final AccusePlayerRequest request) {
        List<SpyfallPlayer> spyfallPlayerByIdList = spyfallPlayerRepository.query(new SpyfallPlayerByIdSpecification(request.accusingPlayerId));
        if (spyfallPlayerByIdList.size() != 1) {
            throw new UnsupportedOperationException();
        }
        SpyfallPlayer accusingPlayer = spyfallPlayerByIdList.get(0);
        if (!gameMemory.gamePhase.equals(GamePhase.ACCUSING_PLAYER)) {
            return new AccusePlayerResponse("Nobody called to accuse the spy!");
        }
        if (request.accusingPlayerId.equals(request.accusedPlayerId)) {
            return new AccusePlayerResponse(String.format("%s cannot accuse himself!", accusingPlayer));
        }

        accusingPlayer.makeAccusation(request.accusedPlayerId);
        List<SpyfallPlayer> spyfallPlayerList = spyfallPlayerRepository.query(new SpyfallPlayerSpecification());
        boolean allAccusationsMade = spyfallPlayerList.stream().allMatch(SpyfallPlayer::hasMadeAccusation);
        boolean roundComplete;
        if (allAccusationsMade) {
            Map<String, Integer> accusedMap = new HashMap<>();
            spyfallPlayerList.forEach(player -> {
                accusedMap.putIfAbsent(player.getAccusedPlayerId(), 0);
                accusedMap.put(player.getAccusedPlayerId(), accusedMap.get(player.getAccusedPlayerId()) + 1);
            });

            if (accusedMap.size() == 2) {
                roundComplete = accusedMap.get(request.accusedPlayerId) == 1 || accusedMap.get(request.accusedPlayerId) == spyfallPlayerList.size() - 1;
            } else {
                roundComplete = false;
            }
        } else {
            roundComplete = false;
        }

        if (!roundComplete && allAccusationsMade) {
            gameMemory.gamePhase = GamePhase.ROUND_PLAYING;
            spyfallPlayerList.forEach(SpyfallPlayer::resetAccusation);
        }

        return new AccusePlayerResponse(allAccusationsMade, roundComplete);
    }
}
