package no.rmz.sequencer;

import javax.sound.midi.MidiDevice;


/**
 * Create a  very simple sequence of pings just to check that the midi
 * signals are passed to their recipient.
 */
public  final class EventGenerator {
  
    private final static String IAC_BUS_NAME = "Bus 1";

    public final static void main(final String[] argv) throws SequencerException {

        final MidiDevice midiDevice
                = IacDeviceUtilities.getMidiReceivingDevice(IAC_BUS_NAME);
        final OneNoteSoundGenerator sg = new OneNoteSoundGenerator();
        final PlingPlongSequencer seq
                = new PlingPlongSequencer(midiDevice, sg);
        seq.start();
    }
}
