package net.avdw.spyfall.game.readyplayer;

public class ReadyPlayerNetworkResponse {
    public String message;
    public boolean allPlayersReady;

    @Override
    public String toString() {
        return "NetworkReadyResponse{" +
                "message='" + message + '\'' +
                ", allPlayersReady=" + allPlayersReady +
                '}';
    }

}
