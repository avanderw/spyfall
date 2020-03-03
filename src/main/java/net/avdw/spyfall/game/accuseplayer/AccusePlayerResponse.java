package net.avdw.spyfall.game.accuseplayer;

public final class AccusePlayerResponse {
    private final boolean successful;
    private final boolean allAccusationsMade;
    private final String message;
    private final boolean roundComplete;

    private AccusePlayerResponse() {
        successful = true;
        allAccusationsMade = false;
        roundComplete = false;
        message = "Accusation was successfully recorded.";
    }

    public AccusePlayerResponse(final String message) {
        this.message = message;
        successful = false;
        allAccusationsMade = false;
        roundComplete = false;
    }

    public AccusePlayerResponse(final boolean allAccusationsMade, final boolean roundComplete) {
        this.allAccusationsMade = allAccusationsMade;
        successful = true;
        message = "All accusations have been made.";
        this.roundComplete = roundComplete;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public boolean isNotSuccessful() {
        return !successful;
    }

    public boolean allAccusationsNotMade() {
        return !allAccusationsMade;
    }

    public boolean allAccusationsMade() {
        return allAccusationsMade;
    }

    public boolean isRoundComplete() {
        return roundComplete;
    }

    public String getMessage() {
        return message;
    }
}
