package net.avdw.spyfall.game.memory;

import com.google.inject.Singleton;
import net.avdw.spyfall.game.GamePhase;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class GameMemory {
    public final List<SpyfallPlayer> spyfallPlayerList = new ArrayList<>();
    public GamePhase gamePhase = GamePhase.SETUP_GAME;
    public SpyfallPlayer previousStartingPlayer;
    public SpyfallPlayer currentPlayer;
    public SpyfallPlayer previousSpyPlayer;
    public SpyfallPlayer previousPlayer;
}
