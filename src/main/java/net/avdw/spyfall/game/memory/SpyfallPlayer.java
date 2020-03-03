package net.avdw.spyfall.game.memory;

public class SpyfallPlayer {
    private final String id;
    private final String name;
    private boolean ready = false;
    private boolean callAccuseSpy = false;
    private boolean spy = false;

    public String getAccusedPlayerId() {
        return accusedPlayerId;
    }

    private String accusedPlayerId;

    public SpyfallPlayer(final String id, final String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "SpyfallPlayer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ready=" + ready +
                ", callAccuseSpy=" + callAccuseSpy +
                ", spy=" + spy +
                ", accusedPlayerId='" + accusedPlayerId + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }
}
