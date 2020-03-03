package net.avdw.spyfall.game.cli;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.avdw.spyfall.game.Game;
import picocli.CommandLine;

import java.util.Scanner;

@Singleton
public class SpyfallGameMenu {
    private boolean running = false;
    private final Scanner scanner = new Scanner(System.in);
    private CommandLine gameCommandLine;

    @Inject
    SpyfallGameMenu(@Game final CommandLine gameCommandLine) {
        this.gameCommandLine = gameCommandLine;
    }

    public void start() {
        gameCommandLine.usage(System.out);
        running = true;
        while (running) {
            loop();
        }
    }

    private void loop() {
        String input = scanner.next();
        gameCommandLine.execute(input.split("\\s"));
    }

    public void stop() {
        running = false;
    }
}
