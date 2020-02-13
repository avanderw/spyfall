package net.avdw.spyfall.game.cli;

import com.google.inject.Inject;
import picocli.CommandLine;

@CommandLine.Command(name = "exit", description = "Exit the spyfall game", mixinStandardHelpOptions = true)
public class ExitGameCli implements Runnable {
    @Inject
    private SpyfallGameMenu spyfallGameMenu;

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        spyfallGameMenu.stop();
    }
}
