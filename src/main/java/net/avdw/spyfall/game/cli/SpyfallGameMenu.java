package net.avdw.spyfall.game.cli;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.avdw.spyfall.GuiceFactory;
import picocli.CommandLine;

import java.util.Scanner;

@Singleton
public class SpyfallGameMenu {
    private boolean running = false;
    private final Scanner scanner = new Scanner(System.in);
    private CommandLine commandLine;
    private GuiceFactory guiceFactory;

    @Inject
    SpyfallGameMenu(final GuiceFactory guiceFactory) {
        this.guiceFactory = guiceFactory;
    }

    public void start() {
        if (commandLine == null) {
            commandLine = new CommandLine(SpyfallGameCli.class, guiceFactory);
        }

        running = true;
        while (running) {
            loop();
        }
    }

    private void loop() {
        String input = scanner.next();
        commandLine.execute(input.split("\\s"));
    }

    public void stop() {
        running = false;
    }
}
