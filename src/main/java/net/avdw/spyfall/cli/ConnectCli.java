package net.avdw.spyfall.cli;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import picocli.CommandLine;

@CommandLine.Command(name = "connect", description = "Connect to a spyfall server", mixinStandardHelpOptions = true)
public class ConnectCli  implements Runnable {
    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        Client client = new Client();
        client.start();
        client.connect(5000, "192.168.0.4", 54555, 54777);

        SomeRequest request = new SomeRequest();
        request.text = "Here is the request";
        client.sendTCP(request);

        client.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof SomeResponse) {
                    SomeResponse response = (SomeResponse)object;
                    System.out.println(response.text);
                }
            }
        });
    }
}
