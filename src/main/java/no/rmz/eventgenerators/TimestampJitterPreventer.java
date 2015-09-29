package no.rmz.eventgenerators;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Assumes that data consists of a sequence of text lines 
 * containing patterns like this: 16:40:51.154335.
 */
public class TimestampJitterPreventer {
    
    private final static Pattern pattern =
            Pattern.compile("(\\d{2}):(\\d{2}):(\\d{2}).(\\d{6})");
    
    public static long parseTimestamp(final String timestamp) throws JitterPreventionFailureException {
        checkNotNull(timestamp);
        final Matcher m = pattern.matcher(timestamp);
        if (!m.matches())  {
            throw new JitterPreventionFailureException("Illegal timestamp syntax: " + timestamp);
        }
        
        final long timestampInNanos = 
            (3600000000l * Integer.parseInt(m.group(1)))
          +  ( 60000000l * Integer.parseInt(m.group(2)))
          +  (  1000000l * Integer.parseInt(m.group(3)))
          +  (             Integer.parseInt(m.group(4)));
        
        return timestampInNanos;
    }
    
    private long previousInternalTimestamp = 0;
    private long previousExternalTimestamp = 0;
    
    public void unjitter(long  currentExternalTimestamp) throws JitterPreventionFailureException {
        final long delta =
                Math.abs( currentExternalTimestamp - previousExternalTimestamp);
        // If delta more than 300 millis, just let it through, otherwise
        // we will wait a little.
        if (delta < 300000) {
            
            try {
                Thread.sleep(delta / 1000, (int) (delta % 1000l));
            } catch (InterruptedException ex) {
                throw new JitterPreventionFailureException(ex);
            }
        }
        
        previousInternalTimestamp = System.currentTimeMillis() * 1000;
        previousExternalTimestamp =  currentExternalTimestamp;
    }
}
