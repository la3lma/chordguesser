package no.rmz.sequencer;

import java.io.IOException;
import javax.sound.midi.MidiDevice;
import no.rmz.eventgenerators.PingEveryHalfSecond;
import no.rmz.scales.ChordAndScaleDatabase;
import no.rmz.scales.ScaleBean;
import no.rmz.scales.ScaleCsvReader;


/**
 * Create a  very simple sequence of pings just to check that the midi
 * signals are passed to their recipient.
 */
public  final class EventGenerator {
  
    private final static String IAC_BUS_NAME = "Bus 1";
    
  

    public final static void main(final String[] argv) throws SequencerException, IOException {
        final  ChordAndScaleDatabase chordDb;
        chordDb = ScaleCsvReader.readChordAndScaleDatabaseFromResources();

        final MidiDevice midiDevice
                = IacDeviceUtilities.getMidiReceivingDevice(IAC_BUS_NAME);
        final ScaleBean scale = chordDb.getAllScales().iterator().next();
        final SoundGenerator sg = new RandomScaleToneGenerator(scale);
        final PlingPlongSequencer seq
                = new PlingPlongSequencer(new PingEveryHalfSecond(), midiDevice, sg);
        seq.start();
    }
}
