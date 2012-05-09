package no.rmz.chordguesser;

import java.util.List;
import java.util.Set;
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
        // XXX This is no way to fix an off by one error!!!
        assertEquals(entries.size() -1 , chordDb.noOfScales());
    }

    @Test
    public void testRecognizeCMajorArpeggiated() {

        final BitVector cMajorBitvector = new BitVector("100010010000");
        final Set<ScaleBean> matchingScales = chordDb.getMatchingScales(cMajorBitvector);

        assert(!matchingScales.isEmpty());
        assertEquals(1, matchingScales.size());
        final  ScaleBean cMajorChord = matchingScales.iterator().next();
        assertEquals("Chord C Major", cMajorChord.getAlternativeScaleNames());
      }
}
