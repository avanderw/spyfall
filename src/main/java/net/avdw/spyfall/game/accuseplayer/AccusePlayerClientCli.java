package net.avdw.spyfall.game.accuseplayer;

import com.esotericsoftware.kryonet.Client;
import com.google.inject.Inject;
import picocli.CommandLine;

@CommandLine.Command(name = "accuse", description = "Accuse the spy", mixinStandardHelpOptions = true)
public class AccusePlayerClientCli implements Runnable {
    @Inject
    private Client client;

    @CommandLine.Parameters
    private String id;

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        client.sendTCP(new AccusePlayerNetworkRequest(id));
    }
}
