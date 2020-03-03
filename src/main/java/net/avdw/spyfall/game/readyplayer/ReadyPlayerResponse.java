package net.avdw.spyfall.game.readyplayer;

public final class ReadyPlayerResponse {
    private final String message;
    private final boolean allPlayersReady;

    public ReadyPlayerResponse(final String message, final boolean allPlayersReady) {
        this.message = message;
        this.allPlayersReady = allPlayersReady;
    }

    @Override
    public String toString() {
        return "GameReadyResponse{" +
                "message='" + message + '\'' +
                ", allPlayersReady=" + allPlayersReady +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public boolean isAllPlayersReady() {
        return allPlayersReady;
    }

    public boolean isAllPlayersNotReady() {
        return !allPlayersReady;
    }

}
