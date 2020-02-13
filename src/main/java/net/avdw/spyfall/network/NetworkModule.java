package net.avdw.spyfall.network;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.avdw.property.PropertyResolver;
import net.avdw.spyfall.PropertyKey;
import net.avdw.spyfall.network.client.ClientModule;
import net.avdw.spyfall.network.packet.ReadyPlayerRequest;
import net.avdw.spyfall.network.packet.ReadyPlayerResponse;
import net.avdw.spyfall.network.server.ServerModule;

import java.util.ArrayList;
import java.util.List;

public class NetworkModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ServerModule());
        install(new ClientModule());
    }

    @Provides
    @Singleton
    List<Class<?>> networkPacketList() {
        List<Class<?>> networkPacketList = new ArrayList<>();
        networkPacketList.add(ReadyPlayerRequest.class);
        networkPacketList.add(ReadyPlayerResponse.class);
        return networkPacketList;
    }

    @Provides
    @Singleton
    @NetworkTimeout
    Integer networkTimeout(final PropertyResolver propertyResolver) {
        return Integer.parseInt(propertyResolver.resolve(PropertyKey.NETWORK_TIMEOUT));
    }

    @Provides
    @Singleton
    @TcpPort
    Integer tcpPort(final PropertyResolver propertyResolver) {
        return Integer.parseInt(propertyResolver.resolve(PropertyKey.TCP_PORT));
    }

    @Provides
    @Singleton
    @UdpPort
    Integer udpPort(final PropertyResolver propertyResolver) {
        return Integer.parseInt(propertyResolver.resolve(PropertyKey.UDP_PORT));
    }
}
