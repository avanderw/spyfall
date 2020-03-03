package net.avdw.spyfall.game.exit;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.inject.Inject;
import net.avdw.spyfall.game.cli.SpyfallGameMenu;
import org.tinylog.Logger;

public class ExitGameListener extends Listener {

    @Inject
    ExitGameListener() {
    }
    @Override
    public void disconnected(final Connection connection) {
        Logger.error("Lost connection to the server, hard exiting now...");
        System.exit(-1);
    }
}
