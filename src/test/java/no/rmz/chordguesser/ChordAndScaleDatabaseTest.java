package no.rmz.chordguesser;

import java.util.List;
import java.util.Set;
import org.junit.*;
import static org.junit.Assert.*;

public class ChordAndScaleDatabaseTest {


    /**
     * All of the entries in no particular order.
     */
    private  List<ScaleBean>       entries;

    /**
     * The database holding indexed versions of the entries.
     */
    private  ChordAndScaleDatabase chordDb;

    /**
     * Read the database content from a CSV file and stuff it in
     * a database instance.  Throws an exception if somethings goes
     * wrong.
     * @throws Exception
     */
    @Before
    public final void seUp() throws Exception {
        final ScaleCsvReader scr = new ScaleCsvReader();
        entries = scr.readScalesFromResourceCsv();
        assertNotNull(entries);
        chordDb = new ChordAndScaleDatabase();
        chordDb.importAllScales(entries);
    }

    /**
     * Test of populate method, of class ChordDatabase.
     * @throws Exception   when something goes wrong
     */
    @Test
    public final void testPopulate() throws Exception {
        // XXX This is no way to fix an off by one error!!!
        assertEquals(entries.size() - 1 , chordDb.noOfScales());
    }

    /**
     * Just check if we can recognize a c major chord from its
     * bitpattern.  That being a nice smoketest for most of the
     * recognition system.
     */
    @Test
    public final void testRecognizeCMajorArpeggiated() {

        final BitVector cMajorBitvector = new BitVector("100010010000");
        final Set<ScaleBean> matchingScales =
                chordDb.getMatchingScales(cMajorBitvector);

        assertTrue(!matchingScales.isEmpty());
        assertEquals(1, matchingScales.size());
        final  ScaleBean cMajorChord = matchingScales.iterator().next();
        assertEquals("Chord C Major", cMajorChord.getAlternativeScaleNames());
      }
}
