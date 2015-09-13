package no.rmz.chordguesser;

import no.rmz.chordguesser.midi.MidiMessageDecoder;

public final  class ScaleAndChordGeneration {
    
    public final static byte MIDDLE_C_MIDI_ENCODING   = (byte) 60;
    public final static byte MIDI_TONE_DOWN_CNAN_ZERO = (byte) 0x90;

     /**
     * Inject a midi sequence into a message decoder
     *
     * @param decoder
     * @param sequence
     */
    public static void inject(
            final MidiMessageDecoder decoder,
            final byte[][] sequence) {
        long timestamp = 0;
        for (final byte[] signal : sequence) {
            decoder.decode(signal, timestamp++);
        }
    }

    /**
     * From a vector of tones, generate a midi sequence of tones
     *
     * @param tones vector of tones.
     * @return Vector of tone down events for channen zero, for the tones in the
     * input vector.
     */
    public static byte[][] midiToneDownSequenceForTones(byte[] tones) {
        final byte[][] result = new byte[tones.length][];
        final byte duration = (byte) 100;
        for (int i = 0; i < tones.length; i++) {
            result[i] = new byte[]{MIDI_TONE_DOWN_CNAN_ZERO, tones[i], duration};
        }
        return result;
    }
    
    
    /**
     * Return a midi sequence for channel zero representing a C major
     * arpeggiated chord.
     * @return 
     */
    public static byte [][] getCMajorChordArpeggiated() {
        byte[][] cMajorChordArpeggiated =
                ScaleAndChordGeneration.midiToneDownSequenceForTones(new byte[]{
                    ScaleAndChordGeneration.MIDDLE_C_MIDI_ENCODING,
                    ScaleAndChordGeneration.MIDDLE_C_MIDI_ENCODING + 4,
                    ScaleAndChordGeneration.MIDDLE_C_MIDI_ENCODING + 7});
        return cMajorChordArpeggiated;
    }
}
