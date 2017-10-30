package no.rmz.chordguesser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;
import no.rmz.bitvectors.BitVector;
import no.rmz.chordguesser.midi.MidiHandler;
import no.rmz.scales.ChordAndScaleDatabase;
import no.rmz.scales.ScaleBean;
import no.rmz.scales.ScaleCsvReader;

/**
 * Midi events and have them printed out in a nice manner, then those events
 * should be harvested into a proper set of unit tests that sends note on/off
 * events to a ToneListener, which in turn updates a PolyphonicState, which in
 * turn triggers some chord and scale detection routines.
 */
public final class ChordGuesser {

    private final static Logger LOG =
            Logger.getLogger(ChordGuesser.class.getName());
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        new ChordGuesser().run();
        Thread.currentThread().join();
    }
    private final ChordAndScaleDatabase chordDb;

    public ChordGuesser() throws FileNotFoundException, IOException {
        this.chordDb = ScaleCsvReader.readChordAndScaleDatabaseFromResources();
    }


    private void run() {
        final MidiHandler mh =
                new MidiHandler(new ReportingNoteListener(chordDb));
        mh.run();
    }

    private static final class ReportingNoteListener implements NoteListener {

        final ChordAndScaleDatabase db;
        final BitVector bv = new BitVector(12);
        final PolyphonicState ps = new PolyphonicState();
        private final Object monitor = new Object();

        ReportingNoteListener(final ChordAndScaleDatabase db) {
            this.db = db;
        }

        @Override
        public void noteOff(int i) {
            synchronized (monitor) {
                final int scaleTone = i % 12;
                bv.unset(scaleTone);
                // LOG.info("noteOff: " + i);
                ps.noteOff(i);
                reportScales();
            }
        }

        @Override
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
            matchingScales.stream().forEach((sb) -> {
                System.out.format("   %s (%s)",
                        sb.getNameOfScale(),
                        sb.getAlternativeScaleNames());
            });
        }
    }
}
