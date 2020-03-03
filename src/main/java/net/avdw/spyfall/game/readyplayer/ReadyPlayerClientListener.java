package net.avdw.spyfall.game.readyplayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.inject.Singleton;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerNetworkResponse;
import org.tinylog.Logger;

@Singleton
public class ReadyPlayerClientListener extends Listener {
    @Override
    public void received(final Connection connection, final Object object) {
        if (object instanceof ReadyPlayerNetworkResponse) {
            Logger.debug("ReadyPlayerResponse");
        }
    }
}
