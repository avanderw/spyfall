package net.avdw.property;

import com.google.inject.Inject;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileWriter {
    private final Properties defaultProperties;

    @Inject
    PropertyFileWriter(@Default final Properties defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public void write(final File file, final String title) {
        try {
            defaultProperties.store(new FileWriter(file), title);
            Logger.info("Wrote property file [{}]", file.getAbsolutePath());
        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.debug(e);
        }
    }
}
