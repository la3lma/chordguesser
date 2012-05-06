
package no.rmz.chordguesser;

/**
 * Listen for tones.  The tones are encoded in the same way
 * as MIDI tone values (0-127), but in addition we are assuming
 * that these are all tempered tuned tones. No weirdness anywhere.
 */
public interface ToneListener {

    void noteOff(int i);

    void noteOn(int i);

}
