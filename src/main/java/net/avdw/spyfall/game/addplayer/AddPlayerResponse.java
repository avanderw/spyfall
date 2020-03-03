package net.avdw.spyfall.game.addplayer;

import net.avdw.spyfall.game.memory.SpyfallPlayer;

public final class AddPlayerResponse {
    private final String message;
    private final boolean successful;
    public SpyfallPlayer spyfallPlayer;

    public AddPlayerResponse(final String message) {
        this.message = message;
        successful = false;
    }

    public AddPlayerResponse() {
        successful = true;
        message = "Player was added.";
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "GameAddPlayerResponse{" +
                "message='" + message + '\'' +
                ", successful=" + successful +
                '}';
    }

    public boolean isNotSuccessful() {
        return !successful;
    }
}
