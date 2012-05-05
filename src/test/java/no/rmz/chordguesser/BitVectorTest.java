package no.rmz.chordguesser;

import org.junit.*;
import static org.junit.Assert.*;

public class BitVectorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testZeroNoOfBits() {
        new BitVector(0);
    }

    @Test
    public void testReadZero() {
        int bit = 0;
        final BitVector instance = new BitVector(1);
        assertFalse("Expected zero-bit to be false", instance.read(0));
    }

    @Test
    public void testSet() {
        int bit = 0;
        final BitVector instance = new BitVector(1);
        instance.set(0);
        assertTrue("Expected zero-bit to be false", instance.read(0));
    }

    @Test
    public void testUnset() {
        final BitVector instance = new BitVector(1);
        instance.set(0);
        instance.unset(0);
        assertFalse("Expected zero-bit to be false", instance.read(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooLargeIndex() {
        final BitVector instance = new BitVector(1);
        instance.set(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooSmallIndex() {
        final BitVector instance = new BitVector(1);
        instance.set(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFromStringIllegalChar() {
        final BitVector instance = new BitVector(1);
        instance.setFromString("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFromStringWrongNoOfBits() {
        final BitVector instance = new BitVector(1);
        instance.setFromString("01");
    }

    public void testReadingAZeroFromString() {
        final BitVector instance = new BitVector(1);
        instance.setFromString("0");
        assertFalse("Expected zero-bit to be false", instance.read(0));
    }

    public void testReadingAOneFromString() {
        final BitVector instance = new BitVector(1);
        instance.setFromString("1");
        assertTrue("Expected zero-bit to be false", instance.read(0));
    }
}
