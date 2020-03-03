package net.avdw.spyfall.game.askquestion;

public class AskQuestionNetworkRequest {
    private String askedPlayerId;

    public AskQuestionNetworkRequest(final String askedPlayerId) {
        this.askedPlayerId = askedPlayerId;
    }

    public String getAskedPlayerId() {
        return askedPlayerId;
    }
}
