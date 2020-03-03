package net.avdw.spyfall.game.askquestion;

import com.esotericsoftware.kryonet.Client;
import com.google.inject.Inject;
import picocli.CommandLine;

@CommandLine.Command(name = "ask", description = "Ask a question to someone", mixinStandardHelpOptions = true)
public class AskQuestionClientCli implements Runnable {
    @Inject
    private Client client;

    @CommandLine.Parameters
    private String id;

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        client.sendTCP(new AskQuestionNetworkRequest(id));
    }
}
