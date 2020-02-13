package net.avdw.spyfall.game;

import net.avdw.spyfall.game.packet.AskResponse;
import net.avdw.spyfall.game.packet.ReadyResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpyfallGameRunner {
    public static void main(String[] args) {
        game1();
        game2();
    }

    public static void game1() {
        SpyfallPlayer player1, player2, player3, player4, player5;

        SpyfallGame spyfallGame = new SpyfallGame();
        spyfallGame.addPlayer(player1 = new SpyfallPlayer(UUID.randomUUID().toString()));
        spyfallGame.addPlayer(player2 = new SpyfallPlayer(UUID.randomUUID().toString()));
        spyfallGame.addPlayer(player3 = new SpyfallPlayer(UUID.randomUUID().toString()));
        spyfallGame.addPlayer(player4 = new SpyfallPlayer(UUID.randomUUID().toString()));
        spyfallGame.addPlayer(player5 = new SpyfallPlayer(UUID.randomUUID().toString()));

        List<SpyfallPlayer> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);
        playerList.add(player5);

        SpyfallGame.StartRoundResponse startRoundResponse = spyfallGame.startRound(); // fail cannot set start player until all ready
        assert startRoundResponse.isNotStarted();

        spyfallGame.ready(player1.getId());
        spyfallGame.ready(player2.getId());
        spyfallGame.ready(player3.getId());
        ReadyResponse readyResponse = spyfallGame.ready(player4.getId());
        assert readyResponse.allPlayersNotReady();
        readyResponse = spyfallGame.ready(player5.getId());
        assert readyResponse.allPlayersReady();

        SpyfallPlayer firstPlayer = spyfallGame.startRound().getFirstPlayer();
        SpyfallPlayer secondPlayer = playerList.stream().filter(p -> !p.equals(firstPlayer)).findAny().orElseThrow(UnsupportedOperationException::new);
        SpyfallPlayer wrongPlayer = playerList.stream().filter(p -> !p.equals(firstPlayer)).filter(p -> !p.equals(secondPlayer)).findAny().orElseThrow(UnsupportedOperationException::new);
        SpyfallPlayer thirdPlayer = playerList.stream().filter(p -> !p.equals(firstPlayer)).filter(p -> !p.equals(secondPlayer)).filter(p -> !p.equals(wrongPlayer)).findAny().orElseThrow(UnsupportedOperationException::new);

        AskResponse askResponse = spyfallGame.ask(wrongPlayer.getId(), secondPlayer.getId(), "Failure: wrong person asking?");
        assert askResponse.isNotSuccessful();
        askResponse = spyfallGame.ask(firstPlayer.getId(), firstPlayer.getId(), "Failure: cannot ask yourself?");
        assert askResponse.isNotSuccessful();
        askResponse = spyfallGame.ask(firstPlayer.getId(), secondPlayer.getId(), "Succeed: right person asking?");
        assert askResponse.isSuccessful();
        askResponse = spyfallGame.ask(secondPlayer.getId(), firstPlayer.getId(), "Failure: cannot ask previous player");
        assert askResponse.isNotSuccessful();
        askResponse = spyfallGame.ask(secondPlayer.getId(), thirdPlayer.getId(), "Succeed: right next person asking");
        assert askResponse.isSuccessful();

        SpyfallGame.AddPlayerResponse addPlayerResponse = spyfallGame.addPlayer(new SpyfallPlayer(UUID.randomUUID().toString())); // fail cannot add people once game has started
        assert addPlayerResponse.isNotSuccessful();

        SpyfallGame.AccuseResponse accuseResponse = spyfallGame.accuse(player1.getId(), player2.getId());
        assert accuseResponse.isNotSuccessful(); // cannot accuse someone if the call to accuse is not made
        SpyfallGame.CallAccuseSpyResponse callAccuseSpyResponse = spyfallGame.callAccuseSpy(firstPlayer.getId());
        assert callAccuseSpyResponse.isSuccessful();
        askResponse = spyfallGame.ask(thirdPlayer.getId(), firstPlayer.getId(), "Failure: cannot ask when guessing spy");
        assert askResponse.isNotSuccessful();

        accuseResponse = spyfallGame.accuse(player1.getId(), player1.getId()); // fail cannot guess yourself
        assert accuseResponse.isNotSuccessful();
        accuseResponse = spyfallGame.accuse(player1.getId(), player2.getId());
        assert accuseResponse.isSuccessful();
        spyfallGame.accuse(player2.getId(), player3.getId());
        spyfallGame.accuse(player3.getId(), player4.getId());
        accuseResponse = spyfallGame.accuse(player4.getId(), player1.getId());
        assert accuseResponse.allAccusationsNotMade();
        accuseResponse = spyfallGame.accuse(player5.getId(), player1.getId());
        assert accuseResponse.isSuccessful();
        assert accuseResponse.allAccusationsMade();

        callAccuseSpyResponse = spyfallGame.callAccuseSpy(firstPlayer.getId()); // fail cannot accuse twice in one round
        assert callAccuseSpyResponse.isNotSuccessful();
        askResponse = spyfallGame.ask(thirdPlayer.getId(), firstPlayer.getId(), "Succeed: can ask after guess");
        assert askResponse.isSuccessful();
        callAccuseSpyResponse = spyfallGame.callAccuseSpy(thirdPlayer.getId());
        assert callAccuseSpyResponse.isSuccessful();
        callAccuseSpyResponse = spyfallGame.callAccuseSpy(secondPlayer.getId()); // fail cannot call accuse when accusing
        assert callAccuseSpyResponse.isNotSuccessful();

        accuseResponse = spyfallGame.accuse(player1.getId(), player2.getId());
        assert accuseResponse.isSuccessful();
        spyfallGame.accuse(player2.getId(), player1.getId());
        spyfallGame.accuse(player3.getId(), player2.getId());
        spyfallGame.accuse(player4.getId(), player2.getId());
        accuseResponse = spyfallGame.accuse(player5.getId(), player2.getId());
        assert accuseResponse.isSuccessful();
        assert accuseResponse.allAccusationsMade();
        assert accuseResponse.isRoundComplete();

        askResponse = spyfallGame.ask(thirdPlayer.getId(), firstPlayer.getId(), "Failure: round has ended");
        assert askResponse.isNotSuccessful();
    }

    public static void game2() {
        SpyfallPlayer player1, player2, player3, player4, player5;

        List<SpyfallPlayer> playerList = new ArrayList<>();
        SpyfallGame spyfallGame = new SpyfallGame();
        spyfallGame.addPlayer(player1 = new SpyfallPlayer(UUID.randomUUID().toString()));
        playerList.add(player1);
        ReadyResponse readyResponse = spyfallGame.ready(player1.getId());
        assert readyResponse.allPlayersReady();

        spyfallGame.addPlayer(player2 = new SpyfallPlayer(UUID.randomUUID().toString()));
        SpyfallGame.StartRoundResponse startRoundResponse = spyfallGame.startRound();
        assert startRoundResponse.isNotStarted(); // cannot start with one player
        playerList.add(player2);
        readyResponse = spyfallGame.ready(player2.getId());
        assert readyResponse.allPlayersReady();
        startRoundResponse = spyfallGame.startRound();
        assert startRoundResponse.isNotStarted(); // cannot start with two players

        spyfallGame.addPlayer(player3 = new SpyfallPlayer(UUID.randomUUID().toString()));
        spyfallGame.addPlayer(player4 = new SpyfallPlayer(UUID.randomUUID().toString()));
        playerList.add(player3);
        playerList.add(player4);
        spyfallGame.ready(player3.getId());
        readyResponse = spyfallGame.ready(player4.getId());
        assert readyResponse.allPlayersReady();

        spyfallGame.addPlayer(player5 = new SpyfallPlayer(UUID.randomUUID().toString()));
        playerList.add(player5);
        spyfallGame.ready(player5.getId());

        startRoundResponse = spyfallGame.startRound();
        SpyfallPlayer firstPlayer = startRoundResponse.getFirstPlayer();
        String spyPlayerId = startRoundResponse.getSpyPlayerId();
        SpyfallPlayer nonSpyPlayer = playerList.stream().filter(p -> !p.getId().equals(spyPlayerId)).findAny().orElseThrow(UnsupportedOperationException::new);

        SpyfallGame.GuessResponse guessResponse = spyfallGame.guess(player1.getId(), "");
        assert guessResponse.isNotSuccessful(); // cannot guess until call for guess has been made
        SpyfallGame.CallGuessLocationResponse callGuessLocationResponse = spyfallGame.callGuessLocation(nonSpyPlayer.getId());
        assert callGuessLocationResponse.isNotSuccessful(); // only spy can call guess
        callGuessLocationResponse = spyfallGame.callGuessLocation(spyPlayerId);
        assert callGuessLocationResponse.isSuccessful();
        callGuessLocationResponse = spyfallGame.callGuessLocation(spyPlayerId);
        assert callGuessLocationResponse.isNotSuccessful(); // cannot call guess location again
        guessResponse = spyfallGame.guess(player1.getId(), "");
        assert guessResponse.isSuccessful();

        AskResponse askQuestionResponse = spyfallGame.ask(firstPlayer.getId(), spyPlayerId, "Failure: round has ended");
        assert askQuestionResponse.isNotSuccessful();
    }
}
