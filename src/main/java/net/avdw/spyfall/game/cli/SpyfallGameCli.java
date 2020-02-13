package net.avdw.spyfall.game.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "spyfall-game", description = "The text interface to the game client", version = "1.0-SNAPSHOT", mixinStandardHelpOptions = true,
        subcommands = {
                ReadyPlayerCli.class,
                ExitGameCli.class})
public class SpyfallGameCli implements Runnable {
    @Override
    public void run() {
        throw new UnsupportedOperationException();
    }
}
