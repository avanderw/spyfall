package net.avdw.spyfall.network;

import com.esotericsoftware.kryonet.Server;
import com.google.inject.Inject;
import net.avdw.spyfall.game.accuseplayer.AccusePlayerServerListener;
import net.avdw.spyfall.game.addplayer.AddPlayerServerListener;
import net.avdw.spyfall.game.askquestion.AskQuestionServerListener;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerServerListener;
import net.avdw.spyfall.game.startround.StartRoundServerListener;

public class ServerListenerConfigurer {
    private final Server server;
    private final AccusePlayerServerListener accusePlayerServerListener;
    private final AddPlayerServerListener addPlayerServerListener;
    private final AskQuestionServerListener askQuestionServerListener;
    private final ReadyPlayerServerListener readyPlayerServerListener;
    private final StartRoundServerListener startRoundServerListener;

    @Inject
    ServerListenerConfigurer(final Server server,
                             final AccusePlayerServerListener accusePlayerServerListener,
                             final AddPlayerServerListener addPlayerServerListener,
                             final AskQuestionServerListener askQuestionServerListener,
                             final ReadyPlayerServerListener readyPlayerServerListener,
                             final StartRoundServerListener startRoundServerListener) {
        this.server = server;
        this.accusePlayerServerListener = accusePlayerServerListener;
        this.addPlayerServerListener = addPlayerServerListener;
        this.askQuestionServerListener = askQuestionServerListener;
        this.readyPlayerServerListener = readyPlayerServerListener;
        this.startRoundServerListener = startRoundServerListener;
    }

    public void configure() {
        server.addListener(accusePlayerServerListener);
        server.addListener(addPlayerServerListener);
        server.addListener(askQuestionServerListener);
        server.addListener(readyPlayerServerListener);
        server.addListener(startRoundServerListener);
    }
}
