package no.rmz.sequencer;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Listen to a file, somwhere, that is being filled with input from TCPdump +
 * possibly other things. Parse it in realtime and generate events that can be
 * parsed musically.
 */
public class TcpdumpEventCatcher {

    private final static String FILENAME = "/tmp/tcpdump.log";

    private final File file;

    public TcpdumpEventCatcher() {
        this(new File(FILENAME));
    }

    public TcpdumpEventCatcher(final File file) {
        this.file = checkNotNull(file);
    }

    public void getEvents() throws FileNotFoundException, IOException {

        try (final FileInputStream fis = new FileInputStream(file);
                final InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                final BufferedReader br = new BufferedReader(isr);) {
            String line;
            fis.skip(-1);
            while ((line = br.readLine()) != null) {
                // Deal with the line
            }
        }

    }
}
