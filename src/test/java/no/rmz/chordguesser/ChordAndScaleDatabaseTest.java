package no.rmz.chordguesser;

import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

public class ChordAndScaleDatabaseTest {
    
    public ChordAndScaleDatabaseTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of populate method, of class ChordDatabase.
     */
    @Test
    public void testPopulate() throws Exception {
        final ScaleCsvReader scr = new ScaleCsvReader();
        final List<ScaleBean> entries = scr.readScalesFromResourceCsv();
        final ChordAndScaleDatabase instance = new ChordAndScaleDatabase();
        instance.importAll(entries);
        // XXX No tests, just keep moving ;)
    }
}
