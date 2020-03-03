package net.avdw.spyfall.game.askquestion;

public class AskQuestionResponse {
    private boolean successful;
    private final String message;

    public AskQuestionResponse() {
        this.successful = true;
        this.message = "Question was asked.";
    }

    public AskQuestionResponse(final String message) {
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
