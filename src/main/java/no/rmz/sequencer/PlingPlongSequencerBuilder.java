package no.rmz.sequencer;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.sound.midi.MidiDevice;


public class PlingPlongSequencerBuilder {
    private EventSource ss;
    private MidiDevice device;
    private SoundGenerator sg;

    public PlingPlongSequencerBuilder() {
    }

    public PlingPlongSequencerBuilder setSignalSource(final EventSource ss) {
        this.ss = checkNotNull(ss);
        return this;
    }

    public PlingPlongSequencerBuilder setDevice(MidiDevice device) {
        this.device = device;
        return this;
    }

    public PlingPlongSequencerBuilder setSoundGenerator(final SoundGenerator sg) {
        this.sg = checkNotNull(sg);
        return this;
    }

    public PlingPlongSequencer build() {
        return new PlingPlongSequencer(ss, device, sg);
    }
}
