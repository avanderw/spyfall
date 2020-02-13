package net.avdw.spyfall.game.packet;

public final class ReadyResponse {
    private boolean allPlayersReady;
    private boolean successful;

    public ReadyResponse(final boolean successful, final boolean allPlayersReady) {
        this.successful = successful;
        this.allPlayersReady = allPlayersReady;
    }

    public boolean allPlayersReady() {
        return allPlayersReady;
    }

    public boolean allPlayersNotReady() {
        return !allPlayersReady;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
