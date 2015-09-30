package no.rmz.eventgenerators;

import org.junit.Test;
import static org.junit.Assert.*;


public class TimestampJitterPreventerTest {
    
    public TimestampJitterPreventerTest() {
    }

    @Test
    public void testParseTimestamp() throws JitterPreventionFailureException {
        long parsedTimestamp =
                JitterPreventer.parseTimestamp("16:40:51.154335");
        
        assertTrue(parsedTimestamp > 0);
    }
    
    
    
}
