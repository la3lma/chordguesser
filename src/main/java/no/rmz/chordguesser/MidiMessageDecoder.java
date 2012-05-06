package no.rmz.chordguesser;

public final class MidiMessageDecoder {



    private final static int NIBBLE_LENGTH_IN_BITS = 4;

    private MidiMessageDecoder() {}


    public static byte getHighNibble(byte a) {

        final byte nibble2 = (byte) ((0xF0 & a) / 16);
        return nibble2;
    }

    public static byte getLowNibble(final byte b) {

        final byte nibble1 = (byte) (0x0F & b);
        return nibble1;
    }

    public static MidiCmd getCmdFromByte(final byte b) {
        final byte highNibble = getHighNibble(b);

        switch (highNibble){
            case  0x8:  return MidiCmd.NOTE_OFF;
            case  0x9:  return MidiCmd.NOTE_ON;
            case  0xA:  return MidiCmd.AFTER_TOUCH;
            case  0xB:  return MidiCmd.CONTROL_CHANGE;
            case  0xC:  return MidiCmd.PROGRAM_PATCH_CHANGE;
            case  0xD:  return MidiCmd.CHANNEL_PRESSURE;
            case  0xE:  return MidiCmd.PITCH_WHEEL;
            default: throw new RuntimeException("Unknown MIDI high nibble: " +  highNibble);
        }
    }

     public static int getMidiChannelFromByte(final byte b) {
        return getLowNibble(b);
    }
}
