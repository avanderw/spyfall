package net.avdw.spyfall.game;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import net.avdw.repository.Repository;
import net.avdw.spyfall.game.accuseplayer.*;
import net.avdw.spyfall.game.addplayer.AddPlayerAction;
import net.avdw.spyfall.game.addplayer.AddPlayerRequest;
import net.avdw.spyfall.game.addplayer.AddPlayerResponse;
import net.avdw.spyfall.game.askquestion.AskQuestionAction;
import net.avdw.spyfall.game.askquestion.AskQuestionRequest;
import net.avdw.spyfall.game.askquestion.AskQuestionResponse;
import net.avdw.spyfall.game.guesslocation.*;
import net.avdw.spyfall.game.memory.SpyfallPlayer;
import net.avdw.spyfall.game.memory.SpyfallPlayerMemoryRepository;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerAction;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerRequest;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerResponse;
import net.avdw.spyfall.game.startround.StartRoundAction;
import net.avdw.spyfall.game.startround.StartRoundRequest;
import net.avdw.spyfall.game.startround.StartRoundResponse;

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
        player1 = new SpyfallPlayer(UUID.randomUUID().toString(), "player1");
        player2 = new SpyfallPlayer(UUID.randomUUID().toString(), "player2");
        player3 = new SpyfallPlayer(UUID.randomUUID().toString(), "player3");
        player4 = new SpyfallPlayer(UUID.randomUUID().toString(), "player4");
        player5 = new SpyfallPlayer(UUID.randomUUID().toString(), "player5");

        Injector injector = Guice.createInjector(new TestModule());
        AddPlayerAction addPlayerAction = injector.getInstance(AddPlayerAction.class);
        AddPlayerRequest addPlayerRequest = new AddPlayerRequest();
        addPlayerRequest.playerId = player1.getId();
        addPlayerRequest.playerName = player1.getName();
        addPlayerAction.call(addPlayerRequest);
        addPlayerRequest.playerId = player2.getId();
        addPlayerRequest.playerName = player2.getName();
        addPlayerAction.call(addPlayerRequest);
        addPlayerRequest.playerId = player3.getId();
        addPlayerRequest.playerName = player3.getName();
        addPlayerAction.call(addPlayerRequest);
        addPlayerRequest.playerId = player4.getId();
        addPlayerRequest.playerName = player4.getName();
        addPlayerAction.call(addPlayerRequest);
        addPlayerRequest.playerId = player5.getId();
        addPlayerRequest.playerName = player5.getName();
        addPlayerAction.call(addPlayerRequest);

        List<SpyfallPlayer> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);
        playerList.add(player5);

        StartRoundAction startRoundAction = injector.getInstance(StartRoundAction.class);
        StartRoundRequest startRoundRequest = new StartRoundRequest();
        StartRoundResponse startRoundResponse = startRoundAction.call(startRoundRequest); // fail cannot set start player until all ready
        assert startRoundResponse.isNotStarted();

        ReadyPlayerAction readyPlayerAction = injector.getInstance(ReadyPlayerAction.class);
        ReadyPlayerRequest readyPlayerRequest = new ReadyPlayerRequest();
        readyPlayerRequest.playerId = player1.getId();
        readyPlayerAction.call(readyPlayerRequest);
        readyPlayerRequest.playerId = player2.getId();
        readyPlayerAction.call(readyPlayerRequest);
        readyPlayerRequest.playerId = player3.getId();
        readyPlayerAction.call(readyPlayerRequest);
        readyPlayerRequest.playerId = player4.getId();
        ReadyPlayerResponse readyPlayerResponse = readyPlayerAction.call(readyPlayerRequest);
        assert readyPlayerResponse.isAllPlayersNotReady();
        readyPlayerRequest.playerId = player5.getId();
        readyPlayerResponse = readyPlayerAction.call(readyPlayerRequest);
        assert readyPlayerResponse.isAllPlayersReady();

        SpyfallPlayer firstPlayer = startRoundAction.call(startRoundRequest).getFirstPlayer();
        SpyfallPlayer secondPlayer = playerList.stream().filter(p -> !p.equals(firstPlayer)).findAny().orElseThrow(UnsupportedOperationException::new);
        SpyfallPlayer wrongPlayer = playerList.stream().filter(p -> !p.equals(firstPlayer)).filter(p -> !p.equals(secondPlayer)).findAny().orElseThrow(UnsupportedOperationException::new);
        SpyfallPlayer thirdPlayer = playerList.stream().filter(p -> !p.equals(firstPlayer)).filter(p -> !p.equals(secondPlayer)).filter(p -> !p.equals(wrongPlayer)).findAny().orElseThrow(UnsupportedOperationException::new);

        AskQuestionAction askQuestionAction = injector.getInstance(AskQuestionAction.class);
        AskQuestionRequest askQuestionRequest = new AskQuestionRequest();
        askQuestionRequest.askingPlayerId = wrongPlayer.getId();
        askQuestionRequest.askedPlayerId = secondPlayer.getId();
        AskQuestionResponse askResponse = askQuestionAction.call(askQuestionRequest);//, "Failure: wrong person asking?");
        assert askResponse.isNotSuccessful();


        askQuestionRequest.askingPlayerId = firstPlayer.getId();
        askQuestionRequest.askedPlayerId = firstPlayer.getId();
        askResponse = askQuestionAction.call(askQuestionRequest);//, "Failure: cannot ask yourself?");
        assert askResponse.isNotSuccessful();
        askQuestionRequest.askingPlayerId = firstPlayer.getId();
        askQuestionRequest.askedPlayerId = secondPlayer.getId();
        askResponse = askQuestionAction.call(askQuestionRequest);//, "Succeed: right person asking?");
        assert askResponse.isSuccessful();
        askQuestionRequest.askingPlayerId = secondPlayer.getId();
        askQuestionRequest.askedPlayerId = firstPlayer.getId();
        askResponse = askQuestionAction.call(askQuestionRequest);//, "Failure: cannot ask previous player");
        assert askResponse.isNotSuccessful();
        askQuestionRequest.askingPlayerId = secondPlayer.getId();
        askQuestionRequest.askedPlayerId = thirdPlayer.getId();
        askResponse = askQuestionAction.call(askQuestionRequest);//, "Succeed: right next person asking");
        assert askResponse.isSuccessful();

        addPlayerRequest.playerName = "non-player";
        addPlayerRequest.playerId = UUID.randomUUID().toString();
        AddPlayerResponse addPlayerResponse = addPlayerAction.call(addPlayerRequest); // fail cannot add people once game has started
        assert addPlayerResponse.isNotSuccessful();

        AccusePlayerAction accusePlayerAction = injector.getInstance(AccusePlayerAction.class);
        AccusePlayerRequest accusePlayerRequest = new AccusePlayerRequest();
        accusePlayerRequest.accusingPlayerId = player1.getId();
        accusePlayerRequest.accusedPlayerId = player2.getId();
        AccusePlayerResponse accusePlayerResponse = accusePlayerAction.call(accusePlayerRequest);
        assert accusePlayerResponse.isNotSuccessful(); // cannot accuse someone if the call to accuse is not made

        StartAccusePlayerAction startAccusePlayerAction = injector.getInstance(StartAccusePlayerAction.class);
        StartAccusePlayerRequest startAccusePlayerRequest = new StartAccusePlayerRequest();
        startAccusePlayerRequest.playerId = firstPlayer.getId();
        StartAccusePlayerResponse startAccusePlayerResponse = startAccusePlayerAction.call(startAccusePlayerRequest);
        assert startAccusePlayerResponse.isSuccessful();

        askQuestionRequest.askingPlayerId = thirdPlayer.getId();
        askQuestionRequest.askedPlayerId = firstPlayer.getId();
        askResponse = askQuestionAction.call(askQuestionRequest);//, "Failure: cannot ask when guessing spy");
        assert askResponse.isNotSuccessful();

        accusePlayerRequest.accusingPlayerId = player1.getId();
        accusePlayerRequest.accusedPlayerId = player1.getId();
        accusePlayerAction.call(accusePlayerRequest);
        accusePlayerResponse = accusePlayerAction.call(accusePlayerRequest); // fail cannot guess yourself
        assert accusePlayerResponse.isNotSuccessful();
        accusePlayerRequest.accusingPlayerId = player1.getId();
        accusePlayerRequest.accusedPlayerId = player2.getId();
        accusePlayerResponse = accusePlayerAction.call(accusePlayerRequest);
        assert accusePlayerResponse.isSuccessful();
        accusePlayerRequest.accusingPlayerId = player2.getId();
        accusePlayerRequest.accusedPlayerId = player3.getId();
        accusePlayerAction.call(accusePlayerRequest);
        accusePlayerRequest.accusingPlayerId = player3.getId();
        accusePlayerRequest.accusedPlayerId = player4.getId();
        accusePlayerAction.call(accusePlayerRequest);
        accusePlayerRequest.accusingPlayerId = player4.getId();
        accusePlayerRequest.accusedPlayerId = player1.getId();
        accusePlayerResponse = accusePlayerAction.call(accusePlayerRequest);
        assert accusePlayerResponse.allAccusationsNotMade();
        accusePlayerRequest.accusingPlayerId = player5.getId();
        accusePlayerRequest.accusedPlayerId = player1.getId();
        accusePlayerResponse = accusePlayerAction.call(accusePlayerRequest);
        assert accusePlayerResponse.isSuccessful();
        assert accusePlayerResponse.allAccusationsMade();

        startAccusePlayerRequest.playerId = firstPlayer.getId();
        startAccusePlayerResponse = startAccusePlayerAction.call(startAccusePlayerRequest); // fail cannot accuse twice in one round
        assert startAccusePlayerResponse.isNotSuccessful();
        askQuestionRequest.askingPlayerId = thirdPlayer.getId();
        askQuestionRequest.askedPlayerId = firstPlayer.getId();
        askResponse = askQuestionAction.call(askQuestionRequest);//, "Succeed: can ask after guess");
        assert askResponse.isSuccessful();
        startAccusePlayerRequest.playerId = thirdPlayer.getId();
        startAccusePlayerResponse = startAccusePlayerAction.call(startAccusePlayerRequest);
        assert startAccusePlayerResponse.isSuccessful();
        startAccusePlayerRequest.playerId = secondPlayer.getId();
        startAccusePlayerResponse = startAccusePlayerAction.call(startAccusePlayerRequest); // fail cannot call accuse when accusing
        assert startAccusePlayerResponse.isNotSuccessful();

        accusePlayerRequest.accusingPlayerId = player1.getId();
        accusePlayerRequest.accusedPlayerId = player2.getId();
        accusePlayerResponse = accusePlayerAction.call(accusePlayerRequest);
        assert accusePlayerResponse.isSuccessful();
        accusePlayerRequest.accusingPlayerId = player2.getId();
        accusePlayerRequest.accusedPlayerId = player1.getId();
        accusePlayerAction.call(accusePlayerRequest);
        accusePlayerRequest.accusingPlayerId = player3.getId();
        accusePlayerRequest.accusedPlayerId = player2.getId();
        accusePlayerAction.call(accusePlayerRequest);
        accusePlayerRequest.accusingPlayerId = player4.getId();
        accusePlayerRequest.accusedPlayerId = player2.getId();
        accusePlayerAction.call(accusePlayerRequest);
        accusePlayerRequest.accusingPlayerId = player5.getId();
        accusePlayerRequest.accusedPlayerId = player2.getId();
        accusePlayerResponse = accusePlayerAction.call(accusePlayerRequest);
        assert accusePlayerResponse.isSuccessful();
        assert accusePlayerResponse.allAccusationsMade();
        assert accusePlayerResponse.isRoundComplete();

        askQuestionRequest.askingPlayerId = thirdPlayer.getId();
        askQuestionRequest.askedPlayerId = firstPlayer.getId();
        askResponse = askQuestionAction.call(askQuestionRequest);//, "Failure: round has ended");
        assert askResponse.isNotSuccessful();
    }

    public static void game2() {
        SpyfallPlayer player1, player2, player3, player4, player5;
        player1 = new SpyfallPlayer(UUID.randomUUID().toString(), "player1");
        player2 = new SpyfallPlayer(UUID.randomUUID().toString(), "player2");
        player3 = new SpyfallPlayer(UUID.randomUUID().toString(), "player3");
        player4 = new SpyfallPlayer(UUID.randomUUID().toString(), "player4");
        player5 = new SpyfallPlayer(UUID.randomUUID().toString(), "player5");


        Injector injector = Guice.createInjector(new TestModule());
        AddPlayerAction addPlayerAction = injector.getInstance(AddPlayerAction.class);
        AddPlayerRequest addPlayerRequest = new AddPlayerRequest();

        List<SpyfallPlayer> playerList = new ArrayList<>();
        addPlayerRequest.playerId = player1.getId();
        addPlayerRequest.playerName = player1.getName();
        addPlayerAction.call(addPlayerRequest);
        playerList.add(player1);
        ReadyPlayerAction readyPlayerAction = injector.getInstance(ReadyPlayerAction.class);
        ReadyPlayerRequest readyPlayerRequest = new ReadyPlayerRequest();
        readyPlayerRequest.playerId = player1.getId();
        ReadyPlayerResponse readyPlayerResponse = readyPlayerAction.call(readyPlayerRequest);
        assert readyPlayerResponse.isAllPlayersReady();

        addPlayerRequest.playerId = player2.getId();
        addPlayerRequest.playerName = player2.getName();
        addPlayerAction.call(addPlayerRequest);
        StartRoundAction startRoundAction = injector.getInstance(StartRoundAction.class);
        StartRoundRequest startRoundRequest = new StartRoundRequest();
        StartRoundResponse startRoundResponse = startRoundAction.call(startRoundRequest);
        assert startRoundResponse.isNotStarted(); // cannot start with one player
        playerList.add(player2);
        readyPlayerRequest.playerId = player2.getId();
        readyPlayerResponse = readyPlayerAction.call(readyPlayerRequest);
        assert readyPlayerResponse.isAllPlayersReady();
        startRoundResponse = startRoundAction.call(startRoundRequest);
        assert startRoundResponse.isNotStarted(); // cannot start with two players

        addPlayerRequest.playerId = player3.getId();
        addPlayerRequest.playerName = player3.getName();
        addPlayerAction.call(addPlayerRequest);
        addPlayerRequest.playerId = player4.getId();
        addPlayerRequest.playerName = player4.getName();
        addPlayerAction.call(addPlayerRequest);
        playerList.add(player3);
        playerList.add(player4);
        readyPlayerRequest.playerId = player3.getId();
        readyPlayerAction.call(readyPlayerRequest);
        readyPlayerRequest.playerId = player4.getId();
        readyPlayerResponse = readyPlayerAction.call(readyPlayerRequest);
        assert readyPlayerResponse.isAllPlayersReady();

        addPlayerRequest.playerId = player5.getId();
        addPlayerRequest.playerName = player5.getName();
        addPlayerAction.call(addPlayerRequest);
        playerList.add(player5);
        readyPlayerRequest.playerId = player5.getId();
        readyPlayerAction.call(readyPlayerRequest);

        startRoundResponse = startRoundAction.call(startRoundRequest);
        SpyfallPlayer firstPlayer = startRoundResponse.getFirstPlayer();
        SpyfallPlayer spyPlayer = startRoundResponse.getSpyPlayer();
        SpyfallPlayer nonSpyPlayer = playerList.stream().filter(p -> !p.getId().equals(spyPlayer.getId())).findAny().orElseThrow(UnsupportedOperationException::new);

        GuessLocationAction guessLocationAction = injector.getInstance(GuessLocationAction.class);
        GuessLocationRequest guessLocationRequest = new GuessLocationRequest();
        guessLocationRequest.playerId = player1.getId();
        GuessLocationResponse guessLocationResponse = guessLocationAction.call(guessLocationRequest);
        assert guessLocationResponse.isNotSuccessful(); // cannot guess until call for guess has been made
        StartGuessLocationAction startGuessLocationAction = injector.getInstance(StartGuessLocationAction.class);
        StartGuessLocationRequest startGuessLocationRequest = new StartGuessLocationRequest();
        startGuessLocationRequest.playerId = nonSpyPlayer.getId();
        StartGuessLocationResponse startGuessLocationResponse = startGuessLocationAction.call(startGuessLocationRequest);
        assert startGuessLocationResponse.isNotSuccessful(); // only spy can call guess
        startGuessLocationRequest.playerId = spyPlayer.getId();
        startGuessLocationResponse = startGuessLocationAction.call(startGuessLocationRequest);
        assert startGuessLocationResponse.isSuccessful();
        startGuessLocationResponse = startGuessLocationAction.call(startGuessLocationRequest);
        assert startGuessLocationResponse.isNotSuccessful(); // cannot call guess location again
        guessLocationRequest.playerId = spyPlayer.getId();
        guessLocationResponse = guessLocationAction.call(guessLocationRequest);
        assert guessLocationResponse.isSuccessful();

        AskQuestionAction askQuestionAction = injector.getInstance(AskQuestionAction.class);
        AskQuestionRequest askQuestionRequest = new AskQuestionRequest();
        askQuestionRequest.askingPlayerId = firstPlayer.getId();
        askQuestionRequest.askedPlayerId = spyPlayer.getId();
        AskQuestionResponse askQuestionResponse = askQuestionAction.call(askQuestionRequest);//, "Failure: round has ended");
        assert askQuestionResponse.isNotSuccessful();
    }

    static private class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(new TypeLiteral<Repository<SpyfallPlayer>>() {
            }).to(SpyfallPlayerMemoryRepository.class);
        }
    }
}
