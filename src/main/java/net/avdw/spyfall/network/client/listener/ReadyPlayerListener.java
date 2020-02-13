package net.avdw.spyfall.network.client.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.inject.Singleton;
import net.avdw.spyfall.network.packet.ReadyPlayerResponse;
import org.pmw.tinylog.Logger;

@Singleton
public class ReadyPlayerListener extends Listener {
    @Override
    public void received(final Connection connection, final Object object) {
        if (object instanceof ReadyPlayerResponse) {
            Logger.debug("ReadyPlayerResponse");
        }
    }
}
