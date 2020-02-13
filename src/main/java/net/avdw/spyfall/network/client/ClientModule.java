package net.avdw.spyfall.network.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.avdw.spyfall.network.client.listener.ReadyPlayerListener;

import java.util.List;

public class ClientModule  extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    Client client(final List<Class<?>> networkPacketList,
                  final ReadyPlayerListener readyPlayerListener) {
        Client client = new Client();
        client.addListener(readyPlayerListener);
        Kryo kryo = client.getKryo();
        networkPacketList.forEach(kryo::register);
        return client;
    }
}
