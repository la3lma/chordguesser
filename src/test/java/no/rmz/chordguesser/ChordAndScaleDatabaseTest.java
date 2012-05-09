package no.rmz.chordguesser;

import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

public class ChordAndScaleDatabaseTest {
   
    
    private  List<ScaleBean> entries;
    private  ChordAndScaleDatabase chordDb;
    
    @Before
    public void seUp() throws Exception {
        final ScaleCsvReader scr = new ScaleCsvReader();
        entries = scr.readScalesFromResourceCsv();
        assertNotNull(entries);
        chordDb = new ChordAndScaleDatabase();
        chordDb.importAllScales(entries);
    }

    /**
     * Test of populate method, of class ChordDatabase.
     */
    @Test
    public void testPopulate() throws Exception {
        assertEquals(entries.size() -1 , chordDb.noOfScales());
    }
    
    @Test
    public void testRecognizeCMajorArpeggiated() {
         
  
    }
}
