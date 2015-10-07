package no.rmz.sequencer;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.LinkedList;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import no.rmz.scales.ScaleBean;


public final class RandomScaleToneGenerator implements SoundGenerator {
    private final List<ShortMessage> myMsgs;
    private final ScaleBean myScale;
    

    public RandomScaleToneGenerator(final ScaleBean scale) {
        this.myScale = checkNotNull(scale);
        this.myMsgs = new LinkedList<>();
        try {
            scale.getBinary12notes();
            final String scaletones = scale.getBinary12notes();
            for (int i = 0; i < scaletones.length(); i++) {
                if (scaletones.charAt(i) == '1') {
                    final ShortMessage onMessage = new ShortMessage();
                    onMessage.setMessage(ShortMessage.NOTE_ON, 0, 60 + i, 93);
                    myMsgs.add(onMessage);
                }
            }
        } catch (InvalidMidiDataException ex) {
            throw new IllegalStateException(" couldn't make message", ex);
        }
    }

    @Override
    public void generate(final Receiver recv) {
        final long timeStamp =   -1; // XXX Perhaps use this?
        final ShortMessage myMsg = randomScaleTone();
        recv.send(myMsg, timeStamp);
    }

    private ShortMessage randomScaleTone() {
        final double random = Math.random();
        final double scale  = random * myMsgs.size();
        final int    note = (int) Math.round(scale) % myMsgs.size();
        return myMsgs.get(note);
    }
}
