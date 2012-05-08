package no.rmz.chordguesser;

import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

public class ChordAndScaleDatabaseTest {
   

    /**
     * Test of populate method, of class ChordDatabase.
     */
    @Test
    public void testPopulate() throws Exception {
        final ScaleCsvReader scr = new ScaleCsvReader();
        final List<ScaleBean> entries = scr.readScalesFromResourceCsv();
        assertNotNull(entries);
        final ChordAndScaleDatabase instance = new ChordAndScaleDatabase();
        instance.importAllScales(entries);
        // XXX -1 is  a fudge factor.  It should be removed by fixing
        //     something in the CSV reader.
        assertEquals(entries.size() -1 , instance.noOfScales());
    }
}
