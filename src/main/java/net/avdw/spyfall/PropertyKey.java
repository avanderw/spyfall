package net.avdw.spyfall;

import net.avdw.liquibase.JdbcUrl;
import net.avdw.spyfall.network.NetworkTimeout;
import net.avdw.spyfall.network.TcpPort;
import net.avdw.spyfall.network.UdpPort;

public final class PropertyKey {
    public static final String LOGGING_LEVEL = String.format("%s.level", Logging.class.getCanonicalName());
    public static final String RELEASE_MODE = String.format("%s.release", Main.class.getCanonicalName());
    public static final String DATABASE_PATH = JdbcUrl.class.getCanonicalName();
    public static final String TCP_PORT =  TcpPort.class.getCanonicalName();
    public static final String UDP_PORT = UdpPort.class.getCanonicalName();
    public static final String NETWORK_TIMEOUT = NetworkTimeout.class.getCanonicalName();

    private PropertyKey() {
    }
}
