package net.avdw.spyfall.game.guesslocation;

public final class GuessLocationResponse {
    private final boolean successful;
    private final String message;

    public GuessLocationResponse() {
        successful = true;
        message = "Guess was successfully registered.";
    }

    public GuessLocationResponse(final String message) {
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
