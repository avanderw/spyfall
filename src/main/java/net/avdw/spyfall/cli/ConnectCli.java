package net.avdw.spyfall.cli;

import com.esotericsoftware.kryonet.Client;
import com.google.inject.Inject;
import net.avdw.spyfall.game.cli.SpyfallGameMenu;
import net.avdw.spyfall.network.NetworkTimeout;
import net.avdw.spyfall.network.TcpPort;
import net.avdw.spyfall.network.UdpPort;
import org.pmw.tinylog.Logger;
import picocli.CommandLine;

import java.io.IOException;
import java.net.InetAddress;

@CommandLine.Command(name = "connect", description = "Connect to a spyfall server", mixinStandardHelpOptions = true)
public class ConnectCli implements Runnable {
    @CommandLine.Option(names = "--address")
    private String ip;

    @Inject
    @TcpPort
    private Integer tcpPort;
    @Inject
    @UdpPort
    private Integer udpPort;
    @Inject
    @NetworkTimeout
    private Integer networkTimeout;
    @Inject
    private Client client;
    @Inject
    private SpyfallGameMenu spyfallGameMenu;

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        client.start();
        try {
            if (ip == null) {
                InetAddress address = client.discoverHost(udpPort, networkTimeout);
                Logger.debug("Discovered address {}", address);
                client.connect(networkTimeout, address.getHostAddress(), tcpPort, udpPort);
            } else {
                Logger.debug("Connecting to address {}", ip);
                client.connect(networkTimeout, ip, tcpPort, udpPort);
            }

            spyfallGameMenu.start();
        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.debug(e);
        }
    }
}
