package net.avdw.spyfall.game.packet;

public class AskResponse {
    private boolean successful;
    private final String message;

    public AskResponse() {
        this.successful = true;
        this.message = "Question was asked.";
    }

    public AskResponse(final String message) {
        this.successful = false;
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public boolean isNotSuccessful() {
        return !successful;
    }

    public String getMessage() {
        return message;
    }
}
