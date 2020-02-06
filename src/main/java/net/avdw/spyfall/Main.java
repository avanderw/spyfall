package net.avdw.spyfall;

import com.google.inject.*;
import net.avdw.ProfilingModule;
import net.avdw.Runtime;
import net.avdw.liquibase.DbConnection;
import net.avdw.liquibase.JdbcUrl;
import net.avdw.property.*;
import net.avdw.spyfall.cli.MainCli;
import net.avdw.spyfall.network.NetworkTimeout;
import net.avdw.spyfall.network.TcpPort;
import net.avdw.spyfall.network.UdpPort;
import org.pmw.tinylog.Logger;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public final class Main {
    private Main() {
    }

    public static void main(final String[] args) {
        CommandLine commandLine = new CommandLine(MainCli.class, new GuiceFactory());
        commandLine.execute(args);
    }

    public static final class GuiceFactory implements CommandLine.IFactory {
        private static final Injector INJECTOR = Guice.createInjector(new GuiceModule());

        @Override
        public <K> K create(final Class<K> aClass) {
            return INJECTOR.getInstance(aClass);
        }

        static class GuiceModule extends AbstractModule {
            @Override
            protected void configure() {
                bind(Runtime.class).toInstance(new Runtime());
                bind(List.class).to(LinkedList.class);

                bind(Path.class).annotatedWith(GlobalProperty.class).toInstance(Paths.get(System.getProperty("user.home")).resolve("net.avdw/spyfall.properties"));
                bind(Path.class).annotatedWith(LocalProperty.class).toInstance(Paths.get("").resolve("spyfall.properties"));

                install(new PropertyModule());
                bind(Logging.class).asEagerSingleton();
                install(new ProfilingModule("net.avdw.spyfall", ""));

                bind(SimpleDateFormat.class).toInstance(new SimpleDateFormat("yyyy-MM-dd"));
            }

            @Provides
            @Singleton
            @DefaultProperty
            Properties defaultProperties() {
                Properties properties = new Properties();
                properties.put(PropertyKey.LOGGING_LEVEL, "DEBUG");
                properties.put(PropertyKey.RELEASE_MODE, "false");
                properties.put(PropertyKey.DATABASE_PATH, "spyfall.sqlite");
                properties.put(PropertyKey.TCP_PORT, "54555");
                properties.put(PropertyKey.UDP_PORT, "54777");
                properties.put(PropertyKey.NETWORK_TIMEOUT, "3000");
                return properties;
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

            @Provides
            @Singleton
            @JdbcUrl
            String jdbcUrl(final PropertyResolver propertyResolver) {
                return String.format("jdbc:sqlite:%s", propertyResolver.resolve(PropertyKey.DATABASE_PATH));
            }

            @Provides
            @Singleton
            @DbConnection
            Connection dbConnection(@JdbcUrl final String jdbcUrl) {
                try {
                    return DriverManager.getConnection(jdbcUrl);
                } catch (SQLException e) {
                    Logger.error(e.getMessage());
                    Logger.debug(e);
                    throw new UnsupportedOperationException();
                }
            }
        }
    }
}
