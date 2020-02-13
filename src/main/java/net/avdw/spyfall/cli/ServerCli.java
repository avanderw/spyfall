package net.avdw.spyfall.cli;

import com.esotericsoftware.kryonet.Server;
import com.google.inject.Inject;
import net.avdw.spyfall.network.TcpPort;
import net.avdw.spyfall.network.UdpPort;
import org.pmw.tinylog.Logger;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(name = "server", description = "Start a spyfall server", mixinStandardHelpOptions = true)
public class ServerCli implements Runnable {
    @Inject
    @TcpPort
    private Integer tcpPort;
    @Inject
    @UdpPort
    private Integer udpPort;
    @Inject
    private Server server;

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        server.start();
        try {
            server.bind(tcpPort, udpPort);
        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.debug(e);
        }
    }
}
