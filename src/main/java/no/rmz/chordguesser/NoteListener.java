package no.rmz.chordguesser;

/**
 * Listen for tones.  The tones are encoded in the same way
 * as MIDI tone values (0-127), but in addition we are assuming
 * that these are all tempered tuned tones. No weirdness anywhere.
 */
public interface NoteListener {

    /**
     * Midi note no 'i' is keyed down.
     * @param i the midi note
     */
    void noteOff(int i);

    /**
     * Midi note no 'i' is keyed off.
     * @param i the midi note
     */
    void noteOn(int i);
}
