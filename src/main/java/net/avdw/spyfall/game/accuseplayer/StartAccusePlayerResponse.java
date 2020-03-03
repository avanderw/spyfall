package net.avdw.spyfall.game.accuseplayer;

public final class StartAccusePlayerResponse {
    private final boolean successful;
    private final String message;

    public StartAccusePlayerResponse() {
        successful = true;
        message = "Make your accusations.";
    }

    public StartAccusePlayerResponse(final String message) {
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
