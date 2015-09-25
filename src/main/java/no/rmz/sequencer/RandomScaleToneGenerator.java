package no.rmz.sequencer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;


public final class RandomScaleToneGenerator implements SoundGenerator {
    private final ShortMessage myMsg;
   

    public RandomScaleToneGenerator() {
        this.myMsg = new ShortMessage();
        try {
            // Start playing the note Middle C (60),
            // moderately loud (velocity = 93).
            myMsg.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
        } catch (InvalidMidiDataException ex) {
            throw new IllegalStateException(" couldn't make message", ex);
        }
    }

    @Override
    public void generate(final Receiver recv) {
        final long timeStamp = -1;
        recv.send(myMsg, timeStamp);
    }
}
