package no.rmz.chordguesser;

import java.util.Arrays;

/**
 * Perhaps it is a clever idea to use bitvectors to hold chord states? If so
 * it's nice to have a class to encapsulate them.
 */
public final class BitVector implements Comparable<BitVector> {

    /**
     * Everyone use eight bit bytes these days, but imagine that once that was a
     * configurable number on decent architectures. Ah, those where the days. In
     * remenisance of those days I declare the right to hage 64 bit bytes coded
     * as longs :-)
     */
    private static final int NO_OF_BITS_IN_A_BYTE = 64;
    /**
     * The number of bits.
     */
    private final int lengthInBits;
    /**
     * The number of bytes.
     */
    private final int lengthInBytes;
    /**
     * The eight-bit bytes that holds the bits. Non-set bits are all zero.
     */
    private final long[] bytes;

    /**
     * Throw an IllegalArgumentException if the parameter is not a valid index
     * into a bit.
     *
     * @param bit
     */
    private void checkArg(final int bit) {
        if (bit < 0) {
            throw new IllegalArgumentException("Index less than zero");
        } else if (bit >= lengthInBits) {
            throw new IllegalArgumentException("Index larger than  length (" + lengthInBits + ")");
        }
    }

    /**
     * Create a bit vector that is initialized by the bits described in the
     * parameter string.
     *
     * @param initializingString
     */
    public BitVector(final String initializingString) {
        this(initializingString.length());
        setFromString(initializingString);
    }

    /**
     * Create a bitvector with a length specified as number of significant bits.
     * The bits are addressed in the range 0 .. (lengthInBits-1)
     *
     * @param noOfBits
     */
    public BitVector(final int noOfBits) {
        if (noOfBits < 1) {
            throw new IllegalArgumentException("No of bits less than one");
        }
        this.lengthInBits = noOfBits;
        this.lengthInBytes = (noOfBits / NO_OF_BITS_IN_A_BYTE) + 1;
        this.bytes = new long[lengthInBytes];
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
        bytes[by] = (long) (bytes[by] | (1 << bi));
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
        bytes[by] = (long) (bytes[by] & ~(1 << bi));
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

    /**
     * The bit-vector can be set to a bitpattern specified by a string, where
     * the string "010" translates to a bitvector "010" where the leftmotst bit
     * is the bit with index zero. The input string must contain only zeros and
     * ones or an IllegalArgumentException will be thrown.
     *
     * @param bitString
     */
    public void setFromString(final String bitString) {
        if (bitString.length() != lengthInBits) {
            throw new IllegalArgumentException("The input string does not contain the correct number of characters, expected "
                    + lengthInBits + " but got " + bitString.length());
        }

        for (int i = 0, n = bitString.length(); i < n; i++) {
            final char ch = bitString.charAt(i);

            if (ch == '0') {
                unset(i);
            } else if (ch == '1') {
                set(i);
            } else {
                throw new IllegalArgumentException("Unknon char in bit input string: " + ch);
            }
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BitVector other = (BitVector) obj;
        if (this.lengthInBits != other.lengthInBits) {
            return false;
        }
        if (this.lengthInBytes != other.lengthInBytes) {
            return false;
        }
        if (!Arrays.equals(this.bytes, other.bytes)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.lengthInBits;
        hash = 37 * hash + this.lengthInBytes;
        hash = 37 * hash + Arrays.hashCode(this.bytes);
        return hash;
    }

    /**
     * Combine the results of op1 and op2 into result. All the four operators
     * must be of equal length for this operation to succeed.
     */
    static void and(final BitVector result, final BitVector op1, final BitVector op2) {
        if (result.lengthInBits != op1.lengthInBits || result.lengthInBits != op2.lengthInBits) {
            throw new IllegalArgumentException("both operands and the result must have the same number of bits");
        }

        for (int i = 0; i < result.lengthInBytes; i++) {
            result.bytes[i] = (long) ((long) op1.bytes[i] & (long) op2.bytes[i]);
        }
    }

    @Override
    public int compareTo(final BitVector other) {
        if (lengthInBits != lengthInBits) {
            throw new IllegalArgumentException("Attempt to compare bit vectors of different lengths");
        }

        for (int i = 0; i < lengthInBytes; i++) {
            final long a = bytes[i];
            final long b = other.bytes[i];
            if (a < b) {
                return -1;
            } else if (a > b) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BitVector = ");
        for (int i = 0; i < lengthInBits; i++) {
            sb.append(read(i) ? "1" : "0");
        }

        return sb.toString();
    }
}
