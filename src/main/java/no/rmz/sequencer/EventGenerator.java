package no.rmz.sequencer;

import javax.sound.midi.MidiDevice;


public  final class EventGenerator {
    
    private final static String IAC_BUS_NAME = "Bus 1";
    
    public final static void main(final String[] argv) throws  SequencerException{
      
        final MidiDevice midiDevice = IacDeviceUtilities.getMidiReceivingDevice(IAC_BUS_NAME);
        final PlingPlongSequencer seq = new PlingPlongSequencer(midiDevice);
        seq.start();
    }
}
