package no.rmz.eventgenerators;

import static org.junit.Assert.*;
import org.junit.Test;


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
