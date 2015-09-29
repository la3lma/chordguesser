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
public class TcpdumpFileReadingEventGenerator implements EventSource {

    private final File file;
    private final EventDistributor eventDistributor;

    public TcpdumpFileReadingEventGenerator(final File file) {
        this.file = checkNotNull(file);
        this.eventDistributor = new EventDistributor();
    }

    public final static class TcpdumpEvent {
        final Long timestamp;

        public TcpdumpEvent(final String line) throws JitterPreventionFailureException {
            // Parse the line as a tcpdump output line
            if (line == null) {
                this.timestamp = null;
            } else {
                final String[] split = line.split(" +");
                final String timestampString = split[0];
                if (timestampString == null || timestampString.trim().isEmpty()) {
                    this.timestamp = null;
                } else {
                    this.timestamp
                            = TimestampJitterPreventer.parseTimestamp(timestampString);
                }
            }
        }

        public Long getTimestamp() {
            return timestamp;
        }

        
        public boolean isValid() {
            return this.timestamp != null;
        }
    }
    
    public static final String[]  parseTcpdumpLine(final String line) {
        // Parse the line as a tcpdump output line
        return line.split(" +");
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
       
            final TimestampJitterPreventer tjp = new TimestampJitterPreventer();
     
            while (file.exists()) {
                final String line = br.readLine();
                final TcpdumpEvent  event = new TcpdumpEvent(line);
                
                if (event.isValid()) {
                    tjp.unjitter(event.getTimestamp());
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
    public void addReceiver(Runnable runnable
    ) {
        eventDistributor.add(runnable);
    }
    }
