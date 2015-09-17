package no.rmz.sequencer;

import javax.sound.midi.MidiUnavailableException;


public  final class EventGenerator {
    
    private final static String IAC_DEVICE_NAME = "IAC devices";
    private final static String IAC_BUS_NAME = "Bus 1";
    
    public final static void main(final String[] argv) throws MidiUnavailableException, SequencerException{
      
        IacDeviceUtilities.listReceiverDevices();
        /*
        final MidiDevice midiDevice = 
                IacDeviceUtilities.getMidiDevice(IAC_DEVICE_NAME, IAC_BUS_NAME);
        final HackedUpSequencer seq = new HackedUpSequencer(midiDevice);
        seq.start();
        */
    }
}
