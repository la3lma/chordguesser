package no.rmz.sequencer;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

public class HackedUpSequencer {

    private final String name;

    private final MidiDevice device;
    private final Receiver rcvr;
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

        SoundGenerator sg = new SoundGenerator(rcvr);
  
        this.process = new RecurringProcess(() -> sg.generate());
    }

    
    public final static void startSequencer(final MidiDevice device) {

        final HackedUpSequencer hackedUpSequencer
                = new HackedUpSequencer(device);

        hackedUpSequencer.process.start();
    }
}
