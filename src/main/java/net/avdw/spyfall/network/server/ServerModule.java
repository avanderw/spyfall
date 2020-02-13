package net.avdw.spyfall.network.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.avdw.spyfall.network.server.listener.AddPlayerListener;
import net.avdw.spyfall.network.server.listener.ReadyPlayerListener;

import java.util.List;

public class ServerModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    Server server(final List<Class<?>> networkPacketList,
                  final AddPlayerListener addPlayerListener,
                  final ReadyPlayerListener readyPlayerListener) {
        Server server = new Server();
        Kryo kryo = server.getKryo();
        networkPacketList.forEach(kryo::register);
        server.addListener(addPlayerListener);
        server.addListener(readyPlayerListener);
        return server;
    }
}
