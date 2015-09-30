package no.rmz.eventgenerators;

/**
 * Parses output from Tcpdump.
 */
public final class TcpdumpEvent implements ParsedEvent {

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
                this.timestamp = JitterPreventer.parseTimestamp(timestampString);
            }
        }
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean isValid() {
        return this.timestamp != null;
    }

}
