package net.avdw.spyfall.game.startround;

import net.avdw.spyfall.game.memory.SpyfallPlayer;

public final class StartRoundResponse {
    private final SpyfallPlayer firstPlayer;
    private final SpyfallPlayer spyPlayer;
    private final boolean started;
    private final String message;

    public StartRoundResponse(final SpyfallPlayer firstPlayer, final SpyfallPlayer spyPlayer) {
        this.firstPlayer = firstPlayer;
        this.spyPlayer = spyPlayer;
        started = true;
        message = "Round was started.";
    }

    public StartRoundResponse(final String message) {
        firstPlayer = null;
        spyPlayer = null;
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

    public SpyfallPlayer getSpyPlayer() {
        return spyPlayer;
    }

    public String getMessage() {
        return message;
    }
}
