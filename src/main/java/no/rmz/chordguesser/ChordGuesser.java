package no.rmz.chordguesser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Midi events and have them printed out in a nice manner, then those events
 * should be harvested into a proper set of unit tests that sends note on/off
 * events to a ToneListener, which in turn updates a PolyphonicState, which in
 * turn triggers some chord and scale detection routines.
 */
public final class ChordGuesser {

    private final static Logger LOG =
            Logger.getLogger(ChordGuesser.class.getName());
    private final ChordAndScaleDatabase chordDb;

    public ChordGuesser() throws FileNotFoundException, IOException {
        final ScaleCsvReader scr = new ScaleCsvReader();
        final List<ScaleBean> beanlist = scr.readScalesFromResourceCsv();

        chordDb = new ChordAndScaleDatabase();
        chordDb.importAllScales(beanlist);
    }

    private final static class ReportingNoteListener implements NoteListener {

        final ChordAndScaleDatabase db;
        final BitVector bv = new BitVector(12);
        final PolyphonicState ps = new PolyphonicState();
        private final Object monitor = new Object();

        public ReportingNoteListener(final ChordAndScaleDatabase db) {
            this.db = db;
        }

        public void noteOff(int i) {
            synchronized (monitor) {
                final int scaleTone = i % 12;
                bv.unset(scaleTone);
                // LOG.info("noteOff: " + i);
                ps.noteOff(i);
                reportScales();
            }
        }

        public void noteOn(int i) {
            synchronized (monitor) {
                final int scaleTone = i % 12;
                bv.set(scaleTone);
                // LOG.info("noteOn: " + i);
                ps.noteOn(i);
                reportScales();
            }
        }

        private void reportScales() {
            System.out.println(bv.toString());
            final Set<ScaleBean> matchingScales = db.getMatchingScales(bv);
            for (final ScaleBean sb : matchingScales) {
                System.out.format("   %s (%s)",
                        sb.getNameOfScale(),
                        sb.getAlternativeScaleNames());
            }
        }
    }

    private void run() {
        final MidiHandler mh =
                new MidiHandler(new ReportingNoteListener(chordDb));
        mh.run();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        new ChordGuesser().run();
        Thread.currentThread().join();
    }
}
