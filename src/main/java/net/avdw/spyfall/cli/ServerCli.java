package net.avdw.spyfall.cli;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import picocli.CommandLine;

@CommandLine.Command(name = "server", description = "Start a spyfall server", mixinStandardHelpOptions = true)
public class ServerCli implements Runnable {
    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        Server server = new Server();
        server.start();
        server.bind(54555, 54777);

        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof SomeRequest) {
                    SomeRequest request = (SomeRequest)object;
                    System.out.println(request.text);

                    SomeResponse response = new SomeResponse();
                    response.text = "Thanks";
                    connection.sendTCP(response);
                }
            }
        });
    }
}
