package no.rmz.sequencer;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

public final class PlingPlongSequencer {

    private final String name;
    private final Receiver rcvr;
    private final EventSource ss;

    
    public  PlingPlongSequencer(final EventSource ss, final MidiDevice device, final SoundGenerator sg) {

        checkNotNull(ss);
        checkNotNull(device);
        checkNotNull(sg);
       
        this.name = device.getDeviceInfo().getDescription();
        this.ss = ss;

        try {
            device.open();
            this.rcvr = device.getReceiver();
        } catch (MidiUnavailableException ex) {
            throw new IllegalStateException(name + " MIDI receiver unavailable", ex);
        }

        
        this.ss.addReceiver(() -> {
            sg.generate(this.rcvr);
        });
    }

    
    public void start() {
        ss.start();
    }
}
