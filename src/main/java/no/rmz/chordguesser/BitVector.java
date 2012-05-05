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
    private final int     lengthInBits;

    /**
     * The eight-bit bytes that holds the bits.
     * Non-set bits are all zero.
     */
    private final byte [] bytes;

    
    /**
     * Throw an IllegalArgumentException if the
     * parameter is not a valid index into a bit.
     * @param bit 
     */
    private  void checkArg(final int bit) {
        if (bit < 0) {
            throw new IllegalArgumentException("Index less than zero");
        } else if (bit >= lengthInBits) {
            throw new IllegalArgumentException("Index larger than  length (" + lengthInBits + ")");
        }
    }
    
    /**
     * Create a bitvector with a length specified as number of
     * significant bits.  The bits are addressed in the range
     * 0 .. (lengthInBits-1)
     * @param noOfBits
     */
    public BitVector(final int noOfBits) {
        if (noOfBits < 1) {
            throw new IllegalArgumentException("No of bits less than one");
        }
        this.lengthInBits = noOfBits;
        this.bytes  = new byte[(noOfBits / NO_OF_BITS_IN_A_BYTE) + 1];
    }

    /**
     * Set bit no "bit" in the bitvector to 1.
     *
     * @param bit the bit to set.
     */
    public void set(final int bit) {
        checkArg(bit);
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
        checkArg(bit);
        int by = bit / NO_OF_BITS_IN_A_BYTE;
        int bi = bit % NO_OF_BITS_IN_A_BYTE;
        bytes[by] = (byte) (bytes[by] & ~(1 << bi));
    }
    
      /**
     * Set the bit no "bit" to 0.
     *
     * @param bit the bit to clear.
     */
    public boolean read(final int bit) {
        checkArg(bit);
        int by = bit / NO_OF_BITS_IN_A_BYTE;
        int bi = bit % NO_OF_BITS_IN_A_BYTE;
        return 0 != (bytes[by] & (1 << bi));
    }
}
