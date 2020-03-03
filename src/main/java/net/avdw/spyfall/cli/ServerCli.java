package net.avdw.spyfall.cli;

import com.esotericsoftware.kryonet.Server;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.avdw.spyfall.network.NetworkProperty;
import net.avdw.spyfall.network.ServerListenerConfigurer;
import org.tinylog.Logger;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(name = "server", description = "Start a spyfall server", mixinStandardHelpOptions = true)
public class ServerCli implements Runnable {
    @Inject
    @Named(NetworkProperty.TCP_PORT)
    private Integer tcpPort;
    @Inject
    @Named(NetworkProperty.UDP_PORT)
    private Integer udpPort;
    @Inject
    private Server server;
    @Inject
    private ServerListenerConfigurer serverListenerConfigurer;

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        serverListenerConfigurer.configure();
        server.start();
        try {
            server.bind(tcpPort, udpPort);
        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.debug(e);
        }
    }
}
