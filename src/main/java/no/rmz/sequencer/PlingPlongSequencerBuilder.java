package no.rmz.sequencer;

import javax.sound.midi.MidiDevice;


public class PlingPlongSequencerBuilder {
    private EventSource ss;
    private MidiDevice device;
    private SoundGenerator sg;

    public PlingPlongSequencerBuilder() {
    }

    public PlingPlongSequencerBuilder setSignalSource(EventSource ss) {
        this.ss = ss;
        return this;
    }

    public PlingPlongSequencerBuilder setDevice(MidiDevice device) {
        this.device = device;
        return this;
    }

    public PlingPlongSequencerBuilder setSoundGenerator(SoundGenerator sg) {
        this.sg = sg;
        return this;
    }

    public PlingPlongSequencer build() {
        return new PlingPlongSequencer(ss, device, sg);
    }
}
