package no.rmz.sequencer;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class HackedUpSequencer {

    private final String name;

    private final MidiDevice device;
    private final Receiver rcvr;
    final ShortMessage myMsg;
    private final RecurringProcess process;

    public HackedUpSequencer(final MidiDevice device) {

        checkNotNull(device);
        this.device = device;

        this.name = device.getDeviceInfo().getDescription();

        try {
            device.open();
            this.rcvr = device.getReceiver();
        } catch (MidiUnavailableException ex) {
            throw new IllegalStateException(name + " MIDI receiver unavailable", ex);
        }

        this.myMsg = new ShortMessage();

        try {
            // Start playing the note Middle C (60),
            // moderately loud (velocity = 93).
            myMsg.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
        } catch (InvalidMidiDataException ex) {
            throw new IllegalStateException(name + " couldn't make message", ex);
        }

        this.process = new RecurringProcess(() -> sequenceSomething());
    }

    public final static void startSequencer(final MidiDevice device) {

        final HackedUpSequencer hackedUpSequencer
                = new HackedUpSequencer(device);

        hackedUpSequencer.start();
    }

    private void sequenceSomething() {
        final long timeStamp = -1;
        try {
            rcvr.send(myMsg, timeStamp);
        } catch (Exception e) {
            process.stop();
        }
    }

    private void start() {
        process.start();
    }
}
