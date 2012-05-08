package no.rmz.chordguesser;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  A very database of chords and scales. Fully in-memory
 *  and indexed only as much as has been necessary
 *  to get halfway decent performance.
 */
public final class ChordAndScaleDatabase {

    private final static Logger LOG = Logger.getLogger(ChordAndScaleDatabase.class.getName());

    private final Map<BitVector, ScaleBean> db =
            new TreeMap<BitVector, ScaleBean>();

    public void importScale(final ScaleBean entry) {
        synchronized (db) {
          
            final String binary12notes = entry.getBinary12notes();
            if (binary12notes.length() < 1) {
                System.out.println("Scale with no binary representation: " +  entry.toString());
                LOG.log(Level.WARNING, "Scale with no binary representation: {0}", entry.toString());
            } else {
                final BitVector bv = new BitVector(binary12notes);
                if (db.containsKey(bv)) {
                    throw new IllegalStateException("Attempt to redefine scale " + entry.getNameOfScale());
                }
            }
        }
    }

    public void  importAll (final Collection<ScaleBean> entries) {
        for (final ScaleBean sb: entries) {
            importScale(sb);
        }
    }
}
