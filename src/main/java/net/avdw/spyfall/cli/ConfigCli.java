package net.avdw.spyfall.cli;

import com.google.inject.Inject;
import net.avdw.property.Default;
import org.tinylog.Logger;
import picocli.CommandLine;

import java.io.IOException;
import java.util.Properties;

@CommandLine.Command(name = "config", description = "Configure the application", mixinStandardHelpOptions = true,
subcommands = {ConfigCreateCli.class})
public class ConfigCli implements Runnable {
    @Inject
    @Default
    private Properties defaultProperties;

    /**
     * Entry point for picocli.
     */
    @Override
    public void run() {
        try {
            defaultProperties.store(System.out, "Default properties");
        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.debug(e);
        }
    }
}
