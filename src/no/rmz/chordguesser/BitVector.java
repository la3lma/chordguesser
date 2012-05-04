package no.rmz.chordguesser;

/**
 * Perhaps it is a clever idea to use bitvectors to
 * hold chord states? If so it's nice to have a class
 * to encapsulate them.
 */
public final class BitVector {

    /**
     * Everyone use eight bit bytes these days,
     * but imagine that once that was a configurable number
     * on decent architectures.  Ah, those where the days.
     */
    private static final  int NO_OF_BITS_IN_A_BYTE = 8;

    /**
     * The number of bits.
     */
    private final int     length;

    /**
     * The eight-bit bytes that holds the bits.
     * Non-set bits are all zero.
     */
    private final byte [] bytes;

    /**
     * Create a bitvector with a length specified as number of
     * significant bits.  The bits are addressed in the range
     * 0 .. (length-1)
     * @param l
     */
    public BitVector(final int l) {
        this.length = l;
        this.bytes  = new byte[(l / NO_OF_BITS_IN_A_BYTE) + 1];
    }

    /**
     * Set bit no "bit" in the bitvector to 1.
     *
     * @param bit the bit to set.
     */
    public void set(final int bit) {
        int by = bit / NO_OF_BITS_IN_A_BYTE;
        int bi = bit % NO_OF_BITS_IN_A_BYTE;
        bytes[by] = (byte) (bytes[by] | (1 << bi));
    }

    /**
     * Set the bit no "bit" to 0.
     *
     * @param bit the bit to clear.
     */
    public void unset(final int bit) {
        int by = bit / NO_OF_BITS_IN_A_BYTE;
        int bi = bit % NO_OF_BITS_IN_A_BYTE;
        bytes[by] = (byte) (bytes[by] & ~(1 << bi));
    }
}
