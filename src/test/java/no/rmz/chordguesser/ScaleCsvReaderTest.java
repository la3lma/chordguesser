package no.rmz.chordguesser;

import no.rmz.scales.ScaleCsvReader;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

public class ScaleCsvReaderTest {

    /**
     * Test of readScalesFromResourceCsv method, of class ScaleCsvReader.
     */
    @Test
    public void testReadScalesFromResourceCsv() throws Exception {
        final ScaleCsvReader instance = new ScaleCsvReader();
        final List result = instance.readScalesFromResourceCsv();
        assertEquals(2402, result.size());
    }
}
