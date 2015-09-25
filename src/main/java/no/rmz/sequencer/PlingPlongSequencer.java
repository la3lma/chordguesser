package no.rmz.sequencer;

import no.rmz.execution.RecurringProcess;
import static com.google.common.base.Preconditions.checkNotNull;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

public final class PlingPlongSequencer {

    private final String name;
    private final Receiver rcvr;
    private final RecurringProcess process;

    public  PlingPlongSequencer(final MidiDevice device, final SoundGenerator sg) {

        checkNotNull(device);
        checkNotNull(sg);
       
        this.name = device.getDeviceInfo().getDescription();

        try {
            device.open();
            this.rcvr = device.getReceiver();
        } catch (MidiUnavailableException ex) {
            throw new IllegalStateException(name + " MIDI receiver unavailable", ex);
        }

        this.process = new RecurringProcess(() -> sg.generate(this.rcvr));
    }

    
    public void start() {
        process.start();
    }
}
