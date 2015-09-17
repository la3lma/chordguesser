package no.rmz.sequencer;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;


public  final class EventGenerator {
    
    private final static String IAC_BUS_NAME = "Bus 1";
    
    public final static void main(final String[] argv) throws MidiUnavailableException, SequencerException{
      
        
       
        final MidiDevice midiDevice = IacDeviceUtilities.getMidiReceivingDevice(IAC_BUS_NAME);
        final HackedUpSequencer seq = new HackedUpSequencer(midiDevice);
        seq.start();
       
    }
}
