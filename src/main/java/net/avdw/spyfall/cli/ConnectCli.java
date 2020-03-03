package net.avdw.spyfall.cli;

import com.esotericsoftware.kryonet.Client;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.avdw.spyfall.game.addplayer.AddPlayerNetworkRequest;
import net.avdw.spyfall.game.cli.SpyfallGameMenu;
import net.avdw.spyfall.network.NetworkProperty;
import org.tinylog.Logger;
import picocli.CommandLine;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

@CommandLine.Command(name = "connect", description = "Connect to a spyfall server", mixinStandardHelpOptions = true)
public class ConnectCli implements Runnable {
    @CommandLine.Option(names = "--address")
    private String ip;

    @Inject
    @Named(NetworkProperty.TCP_PORT)
    private Integer tcpPort;
    @Inject
    @Named(NetworkProperty.UDP_PORT)
    private Integer udpPort;
    @Inject
    @Named(NetworkProperty.NETWORK_TIMEOUT)
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


            Scanner scanner = new Scanner(System.in);
            System.out.println("What is your alias?");
            String name = scanner.next();
            AddPlayerNetworkRequest addPlayerNetworkRequest = new AddPlayerNetworkRequest();
            addPlayerNetworkRequest.playerName = name;
            client.sendTCP(addPlayerNetworkRequest);

            spyfallGameMenu.start();
        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.debug(e);
        }
    }
}
