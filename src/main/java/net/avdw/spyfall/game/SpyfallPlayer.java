package net.avdw.spyfall.game;

public class SpyfallPlayer {
    private final String id;
    private boolean ready = false;
    private boolean callAccuseSpy = false;
    private boolean spy = false;

    public String getAccusedPlayerId() {
        return accusedPlayerId;
    }

    private String accusedPlayerId;

    public SpyfallPlayer(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isNotReady() {
        return !ready;
    }

    public boolean hasMadeAccusation() {
        return this.accusedPlayerId != null;
    }

    public void makeReady() {
        ready = true;
    }

    public void makeAccusation(final String accusedPlayerId) {
        this.accusedPlayerId = accusedPlayerId;
    }

    public void callAccuseSpy() {
        callAccuseSpy = true;
    }

    public boolean hasAccusedSpy() {
        return callAccuseSpy;
    }

    public void resetAccusation() {
        accusedPlayerId = null;
    }

    public void makeSpy() {
        spy = true;
    }

    public boolean isNotSpy() {
        return !spy;
    }
}
