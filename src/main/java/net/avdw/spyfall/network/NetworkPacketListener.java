package net.avdw.spyfall.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.google.gson.Gson;
import org.tinylog.Logger;

public class NetworkPacketListener extends Listener {
    private static Gson gson = new Gson();

    @Override
    public void connected(final Connection connection) {
        Logger.debug("{} connected", connection);
    }

    @Override
    public void disconnected(final Connection connection) {
        Logger.debug("{} disconnected", connection);
    }

    @Override
    public void received(final Connection connection, final Object object) {
        if (!(object instanceof FrameworkMessage.KeepAlive)) {
            Logger.trace("{}={}", object.getClass().getSimpleName(), gson.toJson(object));
        }
    }
}
