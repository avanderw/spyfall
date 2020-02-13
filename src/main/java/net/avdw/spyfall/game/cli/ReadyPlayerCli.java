package net.avdw.spyfall.game.cli;

import com.esotericsoftware.kryonet.Client;
import com.google.inject.Inject;
import net.avdw.spyfall.network.packet.ReadyPlayerRequest;
import picocli.CommandLine;

@CommandLine.Command(name = "ready", description = "Ready the connected player", mixinStandardHelpOptions = true)
public class ReadyPlayerCli implements Runnable {
    @Inject
    private Client client;

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        client.sendTCP(new ReadyPlayerRequest());
    }
}
