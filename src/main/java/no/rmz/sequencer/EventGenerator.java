package no.rmz.sequencer;

import java.io.File;
import java.io.IOException;
    import javax.sound.midi.MidiDevice;
import no.rmz.eventgenerators.FileReadingEventGenerator;
import no.rmz.eventgenerators.JitterPreventionFailureException;
import no.rmz.eventgenerators.TcpdumpEvent;
import no.rmz.scales.ChordAndScaleDatabase;
import no.rmz.scales.ScaleBean;
import no.rmz.scales.ScaleCsvReader;

/**
 * Create a very simple sequence of pings just to check that the midi signals
 * are passed to their recipient.
 */
public final class EventGenerator {

    private final static String IAC_BUS_NAME = "Bus 1";
    private final static String FILENAME = "/tmp/tcpdump.log";

    public final static void mains(final String[] argv) throws SequencerException,
            IOException, InterruptedException, JitterPreventionFailureException {
        final ChordAndScaleDatabase chordDb;
        chordDb = ScaleCsvReader.readChordAndScaleDatabaseFromResources();

        final MidiDevice midiDevice
                = IacDeviceUtilities.getMidiReceivingDevice(IAC_BUS_NAME);
        final ScaleBean scale = chordDb.getAllScales().iterator().next();
        final SoundGenerator sg = new RandomScaleToneGenerator(scale);
        final File file = new File(FILENAME);
        final EventParser tcpdumpParser;
        tcpdumpParser = (String str) ->  new TcpdumpEvent(str);
        final PlingPlongSequencer seq;
        
 
        seq = PlingPlongSequencer.newBuilder()
                .setDevice(midiDevice)
                .setSoundGenerator(sg)
                .setSignalSource(
                        new FileReadingEventGenerator(file,
                        tcpdumpParser))
                .build();
        // XXX Now its time to find someting that sounds good :-)
        // a) Logarithmically enveloped percussion track based on 
        //    total input
        // b) The http v.s. https melody
        // c) Arp interruptions
        // d) Printer protocols interruptions.

        seq.start();
        Thread.currentThread().join();
    }
    
    
    
    public final static void main(final String[] argv) throws SequencerException,
            IOException, InterruptedException, JitterPreventionFailureException {
        final ChordAndScaleDatabase chordDb;
        chordDb = ScaleCsvReader.readChordAndScaleDatabaseFromResources();

        final MidiDevice midiDevice
                = IacDeviceUtilities.getMidiReceivingDevice(IAC_BUS_NAME);
        final ScaleBean scale = chordDb.getAllScales().iterator().next();
        final SoundGenerator sg = new RandomScaleToneGenerator(scale);
        final File file = new File(FILENAME);
        final EventParser tcpdumpParser;
        tcpdumpParser = (String str) -> new TcpdumpEvent(str);
        final PlingPlongSequencer seq;

        final EventSource clockedPoller;
        final GatedReceiver gatedReceiver = new GatedReceiver();

        final FileReadingEventGenerator fileReadingEventGenerator
                = new FileReadingEventGenerator(
                        file,
                        tcpdumpParser);
        fileReadingEventGenerator.addReceiver(gatedReceiver);
        clockedPoller = new ClockedPoller(
                gatedReceiver,
                200);
        

        seq = PlingPlongSequencer.newBuilder()
                .setDevice(midiDevice)
                .setSoundGenerator(sg)
                .setSignalSource(clockedPoller)
                .build();
       
        clockedPoller.start();  // Asynch
        seq.start(); // Aynch
        fileReadingEventGenerator.start(); // Synch
        
        Thread.currentThread().join();
    }
}
