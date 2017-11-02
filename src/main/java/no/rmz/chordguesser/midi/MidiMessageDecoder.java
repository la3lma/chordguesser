package no.rmz.chordguesser.midi;

import no.rmz.chordguesser.NoteListener;

public final class MidiMessageDecoder {

    private final static int NIBBLE_LENGTH_IN_BITS = 4;

    public static byte getHighNibble(final byte a) {
        final byte nibble2 = (byte) ((0xF0 & a) / 16); // Use << instead?
        return nibble2;
    }

    public static byte getLowNibble(final byte b) {

        final byte nibble1 = (byte) (0x0F & b);
        return nibble1;
    }

    public static MidiCmd getCmdFromByte(final byte b) {
        final byte highNibble = getHighNibble(b);

        switch (highNibble) {
            case 0x8:
                return MidiCmd.NOTE_OFF;
            case 0x9:
                return MidiCmd.NOTE_ON;
            case 0xA:
                return MidiCmd.AFTER_TOUCH;
            case 0xB:
                return MidiCmd.CONTROL_CHANGE;
            case 0xC:
                return MidiCmd.PROGRAM_PATCH_CHANGE;
            case 0xD:
                return MidiCmd.CHANNEL_PRESSURE;
            case 0xE:
                return MidiCmd.PITCH_WHEEL;
            default:
                throw new RuntimeException("Unknown MIDI high nibble: " + highNibble);
        }
    }

    public static int getMidiChannelFromByte(final byte b) {
        return getLowNibble(b);
    }


    final NoteListener listener;

    public MidiMessageDecoder(final NoteListener listener) {
        // XXX Null check missing!
        this.listener = listener;
    }

    public void decode(final byte[] m, final long timestamp) {
        // XXX Just to see that we're alive. Remove asap.
        // System.out.printf("%02X%02X%02X%02X\n", m[0], m[1], m[2], (m.length == 4) ? m[3] : 0);
        final MidiCmd cmd = getCmdFromByte(m[0]);
        final int chan = getMidiChannelFromByte(m[0]);
        final int strength = m[2];
        if (cmd == MidiCmd.NOTE_ON && strength != 0) {
            // XXX Fail is m has length < 2
            listener.noteOn(m[1]);
        } else if ((cmd == MidiCmd.NOTE_OFF) ||
                   ((cmd == MidiCmd.NOTE_ON)  && strength == 0)) {
            // XXX Fail is m has length < 2
            listener.noteOff(m[1]);
        }
    }
}
