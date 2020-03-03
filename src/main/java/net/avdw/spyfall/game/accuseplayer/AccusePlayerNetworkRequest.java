package net.avdw.spyfall.game.accuseplayer;

public class AccusePlayerNetworkRequest {
    private final String accusedId;

    public AccusePlayerNetworkRequest(final String accusedId) {
        this.accusedId = accusedId;
    }

    public String getAccusedId() {
        return accusedId;
    }
}
