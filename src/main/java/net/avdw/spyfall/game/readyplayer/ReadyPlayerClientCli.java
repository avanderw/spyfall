package net.avdw.spyfall.game.readyplayer;

import com.esotericsoftware.kryonet.Client;
import com.google.inject.Inject;
import org.tinylog.Logger;
import picocli.CommandLine;

@CommandLine.Command(name = "ready", description = "Ready the connected player", mixinStandardHelpOptions = true)
public class ReadyPlayerClientCli implements Runnable {
    @Inject
    private Client client;

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        ReadyPlayerNetworkRequest readyPlayerNetworkRequest = new ReadyPlayerNetworkRequest();
        client.sendTCP(readyPlayerNetworkRequest);
    }
}
