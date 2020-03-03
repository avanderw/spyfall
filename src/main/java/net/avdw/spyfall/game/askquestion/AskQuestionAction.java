package net.avdw.spyfall.game.askquestion;

import com.google.inject.Inject;
import net.avdw.repository.Repository;
import net.avdw.spyfall.game.GamePhase;
import net.avdw.spyfall.game.memory.SpyfallPlayer;
import net.avdw.spyfall.game.Action;
import net.avdw.spyfall.game.memory.GameMemory;
import net.avdw.spyfall.game.memory.SpyfallPlayerByIdSpecification;

import java.util.List;

public class AskQuestionAction implements Action<AskQuestionRequest, AskQuestionResponse> {
    private final GameMemory gameMemory;
    private final Repository<SpyfallPlayer> spyfallPlayerRepository;

    @Inject
    AskQuestionAction(GameMemory gameMemory, Repository<SpyfallPlayer> spyfallPlayerRepository) {
        this.gameMemory = gameMemory;
        this.spyfallPlayerRepository = spyfallPlayerRepository;
    }

    @Override
    public AskQuestionResponse call(final AskQuestionRequest request) {
        if (request.askedPlayerId.equals(request.askingPlayerId)) {
            return new AskQuestionResponse(String.format("%s cannot ask themselves a question!", gameMemory.currentPlayer));
        }
        if (!request.askingPlayerId.equals(gameMemory.currentPlayer.getId())) {
            return new AskQuestionResponse(String.format("Only %s can ask a question!", gameMemory.currentPlayer));
        }
        if (gameMemory.previousPlayer != null && request.askedPlayerId.equals(gameMemory.previousPlayer.getId())) {
            return new AskQuestionResponse(String.format("%s cannot ask %s who just asked them a question!", gameMemory.currentPlayer, gameMemory.previousPlayer));
        }
        if (gameMemory.gamePhase.equals(GamePhase.ACCUSING_PLAYER)) {
            return new AskQuestionResponse(String.format("%s cannot ask questions whilst everyone is accusing the spy!", gameMemory.currentPlayer));
        }
        if (!gameMemory.gamePhase.equals(GamePhase.ROUND_PLAYING)) {
            return new AskQuestionResponse(String.format("%s cannot ask a question as the round has ended!", gameMemory.currentPlayer));
        }

        gameMemory.previousPlayer = gameMemory.currentPlayer;
        List<SpyfallPlayer> spyfallPlayerByIdList = spyfallPlayerRepository.query(new SpyfallPlayerByIdSpecification(request.askedPlayerId));
        if (spyfallPlayerByIdList.size() != 1) {
            throw new UnsupportedOperationException();
        }
        gameMemory.currentPlayer = spyfallPlayerByIdList.get(0);
        return new AskQuestionResponse();
    }
}
