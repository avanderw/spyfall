package net.avdw.spyfall.game.addplayer;

import com.google.gson.Gson;
import com.google.inject.Inject;
import net.avdw.repository.Repository;
import net.avdw.spyfall.game.GamePhase;
import net.avdw.spyfall.game.memory.SpyfallPlayer;
import net.avdw.spyfall.game.Action;
import net.avdw.spyfall.game.memory.GameMemory;
import org.tinylog.Logger;

public class AddPlayerAction implements Action<AddPlayerRequest, AddPlayerResponse> {
    private final GameMemory gameMemory;
    private final Repository<SpyfallPlayer> spyfallPlayerRepository;
    private final Gson gson;

    @Inject
    AddPlayerAction(final GameMemory gameMemory, final Repository<SpyfallPlayer> spyfallPlayerRepository, final Gson gson) {
        this.gameMemory = gameMemory;
        this.spyfallPlayerRepository = spyfallPlayerRepository;
        this.gson = gson;
    }

    @Override
    public AddPlayerResponse call(final AddPlayerRequest request) {
        if (gameMemory.gamePhase.equals(GamePhase.ROUND_PLAYING)) {
            return new AddPlayerResponse(String.format("Cannot add %s to the game while a round is in play!", request.playerName));
        }

        SpyfallPlayer spyfallPlayer = new SpyfallPlayer(request.playerId, request.playerName);
        spyfallPlayerRepository.add(spyfallPlayer);

        return new AddPlayerResponse();
    }
}
