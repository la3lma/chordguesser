package no.rmz.eventgenerators;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.rmz.sequencer.EventSource;

/**
 * Listen to a file, somwhere, that is being filled with input from TCPdump +
 * possibly other things. Parse it in realtime and generate events that can be
 * parsed musically.
 */
public class FileReadingEventGenerator implements EventSource {

    private final File file;
    private final EventDistributor eventDistributor;

    public FileReadingEventGenerator(final File file) {
        this.file = checkNotNull(file);
        this.eventDistributor = new EventDistributor();
    }

    private void getEvents() throws FileNotFoundException, IOException {

        // XXX If file don't exist, busy-wait until it does.
        while (!file.exists()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                throw new RuntimeException("Interrupted", ex);
            }
        }
        final FileInputStream fis = new FileInputStream(file);

        // fis.skip(Integer.MAX_VALUE);
        try (
                final InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                final BufferedReader br = new BufferedReader(isr);) {
            String line;

            while (file.exists()) {
                line = br.readLine();
                if (line != null) {
                    // Deal with the line
                    eventDistributor.broadcast();
                } else {
                    try {
                    // Or wait a little before trying to fetch another.
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException("Interrupted", ex);
                    }
                }
            }
        }
    }

    @Override
    public void start() {
        try {
            getEvents();
        } catch (IOException ex) {
            throw new RuntimeException(ex); // XXX Bad form
        }
    }

    @Override
    public void addReceiver(Runnable runnable
    ) {
        eventDistributor.add(runnable);
    }
}
