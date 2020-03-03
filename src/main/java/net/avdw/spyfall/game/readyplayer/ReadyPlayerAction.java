package net.avdw.spyfall.game.readyplayer;

import com.google.inject.Inject;
import net.avdw.repository.Repository;
import net.avdw.spyfall.game.memory.SpyfallPlayer;
import net.avdw.spyfall.game.Action;
import net.avdw.spyfall.game.memory.SpyfallPlayerByIdSpecification;
import net.avdw.spyfall.game.memory.SpyfallPlayerSpecification;

import java.util.List;

public class ReadyPlayerAction implements Action<ReadyPlayerRequest, ReadyPlayerResponse> {
    private final Repository<SpyfallPlayer> spyfallPlayerRepository;

    @Inject
    ReadyPlayerAction(final Repository<SpyfallPlayer> spyfallPlayerRepository) {
        this.spyfallPlayerRepository = spyfallPlayerRepository;
    }

    @Override
    public ReadyPlayerResponse call(final ReadyPlayerRequest request) {
        List<SpyfallPlayer> spyfallPlayerByIdList = spyfallPlayerRepository.query(new SpyfallPlayerByIdSpecification(request.playerId));
        if (spyfallPlayerByIdList.size() != 1) {
            throw new UnsupportedOperationException();
        }
        SpyfallPlayer spyfallPlayer = spyfallPlayerByIdList.get(0);
        spyfallPlayer.makeReady();

        List<SpyfallPlayer> spyfallPlayerList = spyfallPlayerRepository.query(new SpyfallPlayerSpecification());
        return new ReadyPlayerResponse(
                String.format("%s you are ready to play!", spyfallPlayer.getName()),
                spyfallPlayerList.stream().allMatch(SpyfallPlayer::isReady));
    }
}
