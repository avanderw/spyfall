package net.avdw.spyfall.game;

import com.google.inject.Singleton;
import net.avdw.spyfall.game.packet.AskResponse;
import net.avdw.spyfall.game.packet.ReadyResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Singleton
public class SpyfallGame {
    private final Random random = new Random();
    private final Map<String, SpyfallPlayer> spyfallPlayerMap = new HashMap<>();
    private SpyfallPlayer previousStartingPlayer;
    private SpyfallPlayer currentPlayer;
    private SpyfallPlayer previousPlayer;
    private SpyfallPlayer previousSpy;
    private boolean roundPlaying = false;
    private boolean accusingSpy = false;
    private boolean guessingLocation = false;

    public AddPlayerResponse addPlayer(final SpyfallPlayer spyfallPlayer) {
        if (roundPlaying) {
            return new AddPlayerResponse(String.format("Cannot add %s to the game while a round is in play!", spyfallPlayer));
        }
        spyfallPlayerMap.put(spyfallPlayer.getId(), spyfallPlayer);
        return new AddPlayerResponse();
    }

    public StartRoundResponse startRound() {
        if (spyfallPlayerMap.values().stream().anyMatch(SpyfallPlayer::isNotReady)) {
            return new StartRoundResponse("Cannot start round until all players are ready!");
        }
        if (spyfallPlayerMap.size() <= 2) {
            return new StartRoundResponse("Cannot start round until you have more than three players!");
        }

        if (previousStartingPlayer == null) {
            currentPlayer = spyfallPlayerMap.values().stream().skip(random.nextInt(spyfallPlayerMap.size())).findFirst().orElseThrow(UnsupportedOperationException::new);
            previousStartingPlayer = currentPlayer;
        } else {
            currentPlayer = previousSpy;
        }

        SpyfallPlayer spyPlayer = spyfallPlayerMap.values().stream().skip(random.nextInt(spyfallPlayerMap.size())).findAny().orElseThrow(UnsupportedOperationException::new);
        spyPlayer.makeSpy();
        roundPlaying = true;
        return new StartRoundResponse(currentPlayer, spyPlayer.getId());
    }

    public ReadyResponse ready(final String playerId) {
        spyfallPlayerMap.get(playerId).makeReady();
        return new ReadyResponse(true, spyfallPlayerMap.values().stream().allMatch(SpyfallPlayer::isReady));
    }

    public AskResponse ask(final String askingPlayerId, final String askedPlayerId, final String question) {
        if (askedPlayerId.equals(askingPlayerId)) {
            return new AskResponse(String.format("%s cannot ask themselves a question!", currentPlayer));
        }
        if (!askingPlayerId.equals(currentPlayer.getId())) {
            return new AskResponse(String.format("Only %s can ask a question!", currentPlayer));
        }
        if (previousPlayer != null && askedPlayerId.equals(previousPlayer.getId())) {
            return new AskResponse(String.format("%s cannot ask %s who just asked them a question!", currentPlayer, previousPlayer));
        }
        if (accusingSpy) {
            return new AskResponse(String.format("%s cannot ask questions whilst everyone is accusing the spy!", currentPlayer));
        }
        if (!roundPlaying) {
            return new AskResponse(String.format("%s cannot ask a question as the round has ended!", currentPlayer));
        }

        previousPlayer = currentPlayer;
        currentPlayer = spyfallPlayerMap.get(askedPlayerId);
        return new AskResponse();
    }

    public CallAccuseSpyResponse callAccuseSpy(final String playerId) {
        if (spyfallPlayerMap.get(playerId).hasAccusedSpy()) {
            return new CallAccuseSpyResponse(String.format("%s cannot accuse the spy more than once in a round!", spyfallPlayerMap.get(playerId)));
        }
        if (accusingSpy) {
            return new CallAccuseSpyResponse(String.format("%s cannot call an accusation whilst everyone is accusing the spy!", spyfallPlayerMap.get(playerId)));
        }
        accusingSpy = true;
        spyfallPlayerMap.get(playerId).callAccuseSpy();
        return new CallAccuseSpyResponse();
    }

    public AccuseResponse accuse(final String accusingPlayerId, final String accusedPlayerId) {
        if (!accusingSpy) {
            return new AccuseResponse("Nobody called to accuse the spy!");
        }
        if (accusingPlayerId.equals(accusedPlayerId)) {
            return new AccuseResponse(String.format("%s cannot accuse himself!", spyfallPlayerMap.get(accusedPlayerId)));
        }

        spyfallPlayerMap.get(accusingPlayerId).makeAccusation(accusedPlayerId);
        boolean allAccusationsMade = spyfallPlayerMap.values().stream().allMatch(SpyfallPlayer::hasMadeAccusation);
        boolean roundComplete;
        if (allAccusationsMade) {
            Map<String, Integer> accusedMap = new HashMap<>();
            spyfallPlayerMap.values().forEach(player -> {
                accusedMap.putIfAbsent(player.getAccusedPlayerId(), 0);
                accusedMap.put(player.getAccusedPlayerId(), accusedMap.get(player.getAccusedPlayerId()) + 1);
            });

            if (accusedMap.size() == 2) {
                roundComplete = accusedMap.get(accusedPlayerId) == 1 || accusedMap.get(accusedPlayerId) == spyfallPlayerMap.size() - 1;
            } else {
                roundComplete = false;
            }
        } else {
            roundComplete = false;
        }

        if (!roundComplete && allAccusationsMade) {
            accusingSpy = false;
            spyfallPlayerMap.values().forEach(SpyfallPlayer::resetAccusation);
        }

        return new AccuseResponse(allAccusationsMade, roundComplete);
    }

    public CallGuessLocationResponse callGuessLocation(final String playerId) {
        if (guessingLocation) {
            return new CallGuessLocationResponse(String.format("%s cannot call a guess as a guess has already been called.", spyfallPlayerMap.get(playerId)));
        }

        if (spyfallPlayerMap.get(playerId).isNotSpy()) {
            return new CallGuessLocationResponse(String.format("%s cannot call a guess as they are not the spy", spyfallPlayerMap.get(playerId)));
        }

        guessingLocation = true;
        return new CallGuessLocationResponse();
    }

    public GuessResponse guess(final String playerId, final String locationId) {
        if (!guessingLocation) {
            return new GuessResponse(String.format("%s cannot guess location until they call for a guess!", spyfallPlayerMap.get(playerId)));
        }

        return new GuessResponse();
    }

    static final class StartRoundResponse {
        private final SpyfallPlayer firstPlayer;
        private final boolean started;
        private final String message;
        private final String spyPlayerId;

        private StartRoundResponse(final SpyfallPlayer firstPlayer, final String spyPlayerId) {
            this.firstPlayer = firstPlayer;
            this.spyPlayerId = spyPlayerId;
            started = true;
            message = "Round was started.";
        }

        private StartRoundResponse(final String message) {
            firstPlayer = null;
            spyPlayerId = null;
            started = false;
            this.message = message;
        }

        public SpyfallPlayer getFirstPlayer() {
            if (firstPlayer == null) {
                throw new UnsupportedOperationException();
            }
            return firstPlayer;
        }

        public boolean isStarted() {
            return started;
        }

        public boolean isNotStarted() {
            return !started;
        }

        public String getSpyPlayerId() {
            return spyPlayerId;
        }
    }

    static final class AddPlayerResponse {
        private final String message;
        private final boolean successful;

        private AddPlayerResponse(final String message) {
            this.message = message;
            successful = false;
        }

        private AddPlayerResponse() {
            successful = true;
            message = "Player was added.";
        }

        public boolean isNotSuccessful() {
            return !successful;
        }
    }

    static final class AccuseResponse {
        private final boolean successful;
        private final boolean allAccusationsMade;
        private final String message;
        private final boolean roundComplete;

        private AccuseResponse() {
            successful = true;
            allAccusationsMade = false;
            roundComplete = false;
            message = "Accusation was successfully recorded.";
        }

        private AccuseResponse(final String message) {
            this.message = message;
            successful = false;
            allAccusationsMade = false;
            roundComplete = false;
        }

        private AccuseResponse(final boolean allAccusationsMade, final boolean roundComplete) {
            this.allAccusationsMade = allAccusationsMade;
            successful = true;
            message = "All accusations have been made.";
            this.roundComplete = roundComplete;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public boolean isNotSuccessful() {
            return !successful;
        }

        public boolean allAccusationsNotMade() {
            return !allAccusationsMade;
        }

        public boolean allAccusationsMade() {
            return allAccusationsMade;
        }

        public boolean isRoundComplete() {
            return roundComplete;
        }
    }

    static final class CallAccuseSpyResponse {
        private final boolean successful;
        private final String message;

        private CallAccuseSpyResponse() {
            successful = true;
            message = "Make your accusations.";
        }

        private CallAccuseSpyResponse(final String message) {
            successful = false;
            this.message = message;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public boolean isNotSuccessful() {
            return !successful;
        }
    }

    static final class CallGuessLocationResponse {
        private final boolean successful;
        private final String message;

        private CallGuessLocationResponse() {
            successful = true;
            message = "You can now guess the location.";
        }

        private CallGuessLocationResponse(final String message) {
            this.message = message;
            successful = false;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public boolean isNotSuccessful() {
            return !successful;
        }
    }

    static final class GuessResponse {
        private final boolean successful;
        private final String message;

        private GuessResponse() {
            successful = true;
            message = "Guess was successfully registered.";
        }

        private GuessResponse(final String message) {
            this.message = message;
            successful = false;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public boolean isNotSuccessful() {
            return !successful;
        }
    }
}
