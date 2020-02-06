package net.avdw.spyfall.cli;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.inject.Inject;
import net.avdw.spyfall.network.*;
import net.avdw.spyfall.network.packet.SomeRequest;
import net.avdw.spyfall.network.packet.SomeResponse;
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

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        Client client = new Client();
        Kryo kryo = client.getKryo();
        kryo.register(SomeRequest.class);
        kryo.register(SomeResponse.class);
        client.start();
        try {
            if (ip == null) {
                InetAddress address = client.discoverHost(tcpPort, networkTimeout);
                Logger.debug("Discovered address {}", address);
                client.connect(networkTimeout, address.getHostAddress(), tcpPort, udpPort);
            } else {
                Logger.debug("Connecting to address {}", ip);
                client.connect(networkTimeout, ip, tcpPort, udpPort);
            }
            SomeRequest request = new SomeRequest();
            request.setText("Here is the request");
            client.sendTCP(request);

            client.addListener(new Listener() {
                public void received(final Connection connection, final Object object) {
                    if (object instanceof SomeResponse) {
                        SomeResponse response = (SomeResponse) object;
                        System.out.println(response.getText());
                    }
                }
            });
        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.debug(e);
        }
    }
}
