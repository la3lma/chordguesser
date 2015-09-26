package no.rmz.sequencer;

import javax.sound.midi.Receiver;


public interface SoundGenerator {

    void generate(final Receiver recv);
    
}
