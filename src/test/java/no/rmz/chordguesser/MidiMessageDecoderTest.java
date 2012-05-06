package no.rmz.chordguesser;

import org.junit.*;
import static org.junit.Assert.*;

public final class MidiMessageDecoderTest {

    /**
     * Test of getHighNibble method, of class MidiMessageDecoder.
     */
    @Test
    public void testGetHighNibble() {
        final byte b = (byte)  0xAB;
        final byte expResult = 0xA;
        final byte result = MidiMessageDecoder.getHighNibble(b);
        assertEquals(expResult, result);
    }

    /**
     * Test of getLowNibble method, of class MidiMessageDecoder.
     */
    @Test
    public void testGetLowNibble() {
        final byte b = (byte) 0xAB;
        final byte expResult = 0xB;
        final byte result = MidiMessageDecoder.getLowNibble(b);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCmdFromByte method, of class MidiMessageDecoder.
     */
    @Test
    public void testGetNoteOnCmdFromByte() {
        byte b = (byte) 0x9E;
        final MidiCmd expResult = MidiCmd.NOTE_ON;
        final MidiCmd result = MidiMessageDecoder.getCmdFromByte(b);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCmdFromByte method, of class MidiMessageDecoder.
     */
    @Test
    public void testGetNoteOffCmdFromByte() {
        byte b = (byte) 0x8E;
        final MidiCmd expResult = MidiCmd.NOTE_OFF;
        final MidiCmd result = MidiMessageDecoder.getCmdFromByte(b);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMidiChannelFromByte method, of class MidiMessageDecoder.
     */
    @Test
    public void testGetMidiChannelFromByte() {
        final byte b = (byte) 0x8E;
        final int expResult = (byte) 0xE;
        final int result = MidiMessageDecoder.getMidiChannelFromByte(b);
        assertEquals(expResult, result);
    }
}
