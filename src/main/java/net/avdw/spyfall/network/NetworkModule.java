package net.avdw.spyfall.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.avdw.property.AbstractPropertyModule;
import net.avdw.spyfall.game.accuseplayer.AccusePlayerNetworkRequest;
import net.avdw.spyfall.game.accuseplayer.AccusePlayerNetworkResponse;
import net.avdw.spyfall.game.accuseplayer.AccusePlayerServerListener;
import net.avdw.spyfall.game.addplayer.AddPlayerNetworkRequest;
import net.avdw.spyfall.game.addplayer.AddPlayerNetworkResponse;
import net.avdw.spyfall.game.addplayer.AddPlayerServerListener;
import net.avdw.spyfall.game.askquestion.AskQuestionNetworkRequest;
import net.avdw.spyfall.game.askquestion.AskQuestionNetworkResponse;
import net.avdw.spyfall.game.askquestion.AskQuestionServerListener;
import net.avdw.spyfall.game.exit.ExitGameListener;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerClientListener;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerNetworkRequest;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerNetworkResponse;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerServerListener;
import net.avdw.spyfall.game.startround.StartRoundNetworkRequest;
import net.avdw.spyfall.game.startround.StartRoundNetworkResponse;
import net.avdw.spyfall.game.startround.StartRoundServerListener;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.tinylog.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class NetworkModule extends AbstractPropertyModule {
    @Override
    protected void configure() {
        Properties properties = configureProperties();
        Names.bindProperties(binder(), properties);

        bindInterceptor(Matchers.subclassesOf(Listener.class), new AbstractMatcher<Method>() {
            @Override
            public boolean matches(final Method method) {
                return !method.isSynthetic() && method.getName().equals("received");
            }
        }, invocation -> {
            try {
                return invocation.proceed();
            } catch (RuntimeException e) {
                Logger.error(e.getMessage());
                Logger.debug(e);
                Connection conn = (Connection) invocation.getArguments()[0];
                GenericErrorNetworkResponse genericErrorNetworkResponse = new GenericErrorNetworkResponse();
                genericErrorNetworkResponse.message = e.getMessage() == null || e.getMessage().isEmpty() ? "Something went wrong" : e.getMessage();
                conn.sendTCP(genericErrorNetworkResponse);
                return null;
            }
        });
    }

    @Provides
    @Singleton
    Client client(final List<Class<?>> networkPacketList,
                  final ReadyPlayerClientListener readyPlayerClientListener,
                  final ExitGameListener exitGameListener) {
        Client client = new Client();
        client.addListener(new NetworkPacketListener());
        client.addListener(readyPlayerClientListener);
        client.addListener(exitGameListener);
        Kryo kryo = client.getKryo();
        networkPacketList.forEach(kryo::register);
        return client;
    }

    @Provides
    @Singleton
    Server server(final List<Class<?>> networkPacketList) {
        Server server = new Server();
        Kryo kryo = server.getKryo();
        networkPacketList.forEach(kryo::register);
        server.addListener(new NetworkPacketListener());
        return server;
    }

    @Provides
    @Singleton
    List<Class<?>> networkPacketList() {
        List<Class<?>> networkPacketList = new ArrayList<>();
        networkPacketList.add(AccusePlayerNetworkRequest.class);
        networkPacketList.add(AccusePlayerNetworkResponse.class);
        networkPacketList.add(AddPlayerNetworkRequest.class);
        networkPacketList.add(AddPlayerNetworkResponse.class);
        networkPacketList.add(AskQuestionNetworkRequest.class);
        networkPacketList.add(AskQuestionNetworkResponse.class);
        networkPacketList.add(ReadyPlayerNetworkRequest.class);
        networkPacketList.add(ReadyPlayerNetworkResponse.class);
        networkPacketList.add(StartRoundNetworkRequest.class);
        networkPacketList.add(StartRoundNetworkResponse.class);
        networkPacketList.add(GenericErrorNetworkResponse.class);
        return networkPacketList;
    }

    @Override
    protected Properties defaultProperties() {
        Properties properties = new Properties();
        properties.put(NetworkProperty.TCP_PORT, "54555");
        properties.put(NetworkProperty.UDP_PORT, "54777");
        properties.put(NetworkProperty.NETWORK_TIMEOUT, "3000");
        return properties;
    }
}
