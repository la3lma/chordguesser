package no.rmz.eventgenerators;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import no.rmz.sequencer.EventParser;
import no.rmz.sequencer.EventSource;

/**
 * Listen to a file, somwhere, that is being filled with input from TCPdump +
 * possibly other things. Parse it in realtime and generate events that can be
 * parsed musically.
 */
public class FileReadingEventGenerator implements EventSource {

    private final File file;
    private final EventDistributor eventDistributor;
    private final EventParser parser;

    /**
     * 
     * @param file The file to parse.
     * @param parser Will parse all incoming lines and if the input line was
     *   a valid item, it will be transmitted along the signal path.
     */
    public FileReadingEventGenerator(
            final File file,
            final EventParser parser) {
        this.file = checkNotNull(file);
        this.eventDistributor = new EventDistributor();
        this.parser = checkNotNull(parser);
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

        // fis.skip(Integer.MAX_VALUE); // XXX We reallyshould do this, why don't we?
        try (
                final InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                final BufferedReader br = new BufferedReader(isr);) {

            final JitterPreventer tjp = new JitterPreventer();

            while (file.exists()) {
                final String line = br.readLine();
                final ParsedEvent event = parser.parse(line);

                if (event.isValid()) {
                    tjp.unjitter(event.getTimestamp());
                    eventDistributor.broadcast(event);
                } else {
                    try {
                        // Or wait a little before trying to fetch another.
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException("Interrupted", ex);
                    }
                }
            }
        } catch (JitterPreventionFailureException ex) {
            new RuntimeException(ex); // XXX Exeption(al) crime.
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
    public void addReceiver(final EventReceiver rec) {
        checkNotNull(rec);
        eventDistributor.add(rec);
    }
}
