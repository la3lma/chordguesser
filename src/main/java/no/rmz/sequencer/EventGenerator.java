package no.rmz.sequencer;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.MidiDevice;
import no.rmz.eventgenerators.TcpdumpFileReadingEventGenerator;
import no.rmz.eventgenerators.PingEveryHalfSecond;
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

    public final static void main(final String[] argv) throws SequencerException, 
            IOException, InterruptedException {
        final ChordAndScaleDatabase chordDb;
        chordDb = ScaleCsvReader.readChordAndScaleDatabaseFromResources();

        final MidiDevice midiDevice
                = IacDeviceUtilities.getMidiReceivingDevice(IAC_BUS_NAME);
        final ScaleBean scale = chordDb.getAllScales().iterator().next();
        final SoundGenerator sg = new RandomScaleToneGenerator(scale);
        final File file = new File(FILENAME);
        final PlingPlongSequencer seq
                = new PlingPlongSequencer(
                        new TcpdumpFileReadingEventGenerator(file), midiDevice, sg);
        seq.start();
        Thread.currentThread().join();
    }
}
