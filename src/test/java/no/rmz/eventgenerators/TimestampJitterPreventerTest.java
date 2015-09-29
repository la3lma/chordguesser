package no.rmz.eventgenerators;

import org.junit.Test;
import static org.junit.Assert.*;


public class TimestampJitterPreventerTest {
    
    public TimestampJitterPreventerTest() {
    }

    @Test
    public void testParseTimestamp() throws JitterPreventionFailureException {
        long parsedTimestamp =
                TimestampJitterPreventer.parseTimestamp("16:40:51.154335");
        
        assertTrue(parsedTimestamp > 0);
    }
    
    
    @Test 
    public void testFoo() {
        final String line = 
                "17:21:13.567034 IP ip-172-20-10-3.eu-west-1.compute.internal.51320 > 148.123.29.55.https: UDP, length 1350";
        String[] result = TcpdumpFileReadingEventGenerator.parseTcpdumpLine(line);
        assertEquals("17:21:13.567034", result[0]);
    
    }
}
