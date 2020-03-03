package net.avdw.spyfall;

import net.avdw.spyfall.cli.MainCli;
import org.tinylog.Logger;
import picocli.CommandLine;

public final class Main {
    private Main() {
    }

    public static void main(final String[] args) {
        CommandLine commandLine = new CommandLine(MainCli.class, GuiceFactory.getInstance());
        commandLine.execute(args);
    }
}
