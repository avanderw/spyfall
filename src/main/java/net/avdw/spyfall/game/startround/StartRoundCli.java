package net.avdw.spyfall.game.startround;

import com.esotericsoftware.kryonet.Client;
import com.google.inject.Inject;
import picocli.CommandLine;

@CommandLine.Command(name = "start", description = "Start the round", mixinStandardHelpOptions = true)
public class StartRoundCli implements Runnable {
    @Inject
    private Client client;

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        client.sendTCP(new StartRoundNetworkRequest());
    }
}
