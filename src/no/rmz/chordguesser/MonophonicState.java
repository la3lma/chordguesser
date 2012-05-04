package no.rmz.chordguesser;

/**
 * Represent a single tone.
 */
public final class MonophonicState {
   
    /**
     * The identity of the tone.
     */
    private final int id;
    
    private boolean isOn = false;

    public MonophonicState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
 
    void on() {
        isOn = true;
    }

    void off() {
        isOn = false;
    }
}
