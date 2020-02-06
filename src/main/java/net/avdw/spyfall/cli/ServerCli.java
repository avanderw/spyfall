package net.avdw.spyfall.cli;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.inject.Inject;
import net.avdw.spyfall.network.packet.SomeRequest;
import net.avdw.spyfall.network.packet.SomeResponse;
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

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        Server server = new Server();
        Kryo kryo = server.getKryo();
        kryo.register(SomeRequest.class);
        kryo.register(SomeResponse.class);
        server.start();
        try {
            server.bind(tcpPort, udpPort);
            server.addListener(new Listener() {
                public void received(final Connection connection, final Object object) {
                    if (object instanceof SomeRequest) {
                        SomeRequest request = (SomeRequest) object;
                        System.out.println(request.getText());

                        SomeResponse response = new SomeResponse();
                        response.setText("Thanks");
                        connection.sendTCP(response);
                    }
                }
            });
        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.debug(e);
        }


    }
}
