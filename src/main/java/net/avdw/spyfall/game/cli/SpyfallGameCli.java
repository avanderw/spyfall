package net.avdw.spyfall.game.cli;

import net.avdw.spyfall.game.accuseplayer.AccusePlayerClientCli;
import net.avdw.spyfall.game.askquestion.AskQuestionClientCli;
import net.avdw.spyfall.game.readyplayer.ReadyPlayerClientCli;
import net.avdw.spyfall.game.startround.StartRoundCli;
import picocli.CommandLine;

@CommandLine.Command(name = "spyfall-game", description = "Text interface to the game client", version = "1.0-SNAPSHOT", mixinStandardHelpOptions = true,
        subcommands = {
                ReadyPlayerClientCli.class,
                StartRoundCli.class,
                AskQuestionClientCli.class,
                AccusePlayerClientCli.class,
                ExitGameCli.class})
public class SpyfallGameCli implements Runnable {
    @Override
    public void run() {
        throw new UnsupportedOperationException();
    }
}
