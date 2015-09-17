package no.rmz.sequencer;

import com.google.common.base.Preconditions;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;


public final class SoundGenerator {
    private final Receiver recv;
    private final ShortMessage myMsg;
   

    public SoundGenerator(final Receiver recv) {
        this.recv = Preconditions.checkNotNull(recv);
        this.myMsg = new ShortMessage();
        try {
            // Start playing the note Middle C (60),
            // moderately loud (velocity = 93).
            myMsg.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
        } catch (InvalidMidiDataException ex) {
            throw new IllegalStateException(" couldn't make message", ex);
        }
    }

    public void generate() {
        final long timeStamp = -1;
        recv.send(myMsg, timeStamp);
    }
}
