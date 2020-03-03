package net.avdw.spyfall.game.guesslocation;

public final class StartGuessLocationResponse {
    private final boolean successful;
    private final String message;

    public StartGuessLocationResponse() {
        successful = true;
        message = "You can now guess the location.";
    }

    public StartGuessLocationResponse(final String message) {
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
