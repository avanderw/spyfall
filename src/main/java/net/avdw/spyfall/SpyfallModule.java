package net.avdw.spyfall;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import net.avdw.ProfilingModule;
import net.avdw.Runtime;
import net.avdw.database.DbConnection;
import net.avdw.property.*;
import net.avdw.spyfall.game.Game;
import net.avdw.spyfall.game.GameModule;
import net.avdw.spyfall.game.cli.SpyfallGameCli;
import net.avdw.spyfall.network.NetworkGamePacketMapper;
import net.avdw.spyfall.network.NetworkModule;
import org.tinylog.Logger;
import picocli.CommandLine;
import sun.awt.HKSCS;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

class SpyfallModule extends AbstractPropertyModule {
    @Override
    protected void configure() {
        Properties properties = configureProperties();
        properties.putAll(System.getProperties());
        properties.putAll(System.getenv());
        Names.bindProperties(binder(), properties);

        bind(Runtime.class).toInstance(new Runtime());
        bind(List.class).to(LinkedList.class);

        List<String> excludeMethodList = Arrays.asList("idle", "run");
        install(new ProfilingModule(Matchers.inPackage(Package.getPackage("net.avdw")), new AbstractMatcher<Method>() {
            @Override
            public boolean matches(final Method method) {
                return !method.isSynthetic() && !excludeMethodList.contains(method.getName());
            }
        }));

        bind(SimpleDateFormat.class).toInstance(new SimpleDateFormat("yyyy-MM-dd"));
        bind(NetworkGamePacketMapper.class).toInstance(NetworkGamePacketMapper.INSTANCE);
        install(new NetworkModule());

        bind(CommandLine.class).annotatedWith(Game.class).toInstance(new CommandLine(SpyfallGameCli.class, GuiceFactory.getInstance()));
        install(new GameModule());
    }

    @Override
    protected Properties defaultProperties() {
        Properties properties = new Properties();
        properties.put(SpyfallProperty.LANGUAGE, "EN");
        properties.put(SpyfallProperty.JDBC_URL, "jdbc:sqlite:spyfall.sqlite");
        return properties;
    }

    @Provides
    @Singleton
    @DbConnection
    Connection dbConnection(@Named(SpyfallProperty.JDBC_URL) final String jdbcUrl) {
        try {
            return DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            Logger.debug(e);
            throw new UnsupportedOperationException();
        }
    }
}
