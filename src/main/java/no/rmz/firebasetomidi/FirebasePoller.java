package no.rmz.firebasetomidi;

import java.io.IOException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import no.rmz.eventgenerators.JitterPreventionFailureException;
import no.rmz.sequencer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create a very simple sequence of pings just to check that the midi signals
 * are passed to their recipient.
 *
 *
 * SO: Purpose of this thing is to read from a place in a firebase, get events
 * from there (injected by a shellcript). Set up a full chain based on this,
 * then start adding compositions etc.
 */
public final class FirebasePoller {

    private static final Logger LOG = LoggerFactory.getLogger(FirebasePoller.class);

    /**
     *
     * Firebase Midi Bridge
     *
     * ./fbmidibridge.java foobarbaz foobarbaz.json path virtual-midi-device ...
     * will work inside the foobarbaz firebase application, using the
     * foobarbaz.json security credentials to listen for incoming midi events in
     * the path. Once the events are received, they are removed from firebase,
     * and transmitted as MIDI on the local OSX midi device called
     * virtual-midi-device
     *
     *
     * FBMidiReadingEventGenerator
     *
     * @param argv
     * @throws SequencerException
     * @throws IOException
     * @throws InterruptedException
     * @throws JitterPreventionFailureException
     * @throws javax.sound.midi.MidiUnavailableException
     */
    public final static void main(final String[] argv) throws SequencerException,
            IOException, InterruptedException, JitterPreventionFailureException, MidiUnavailableException {

        LOG.info("Getting started");

        // These should be gotten from the argv
        final String configFile = "fbmidibridge-1746b45f5da7.json";  // arg2
        final String databaseName = "fbmidibridge"; // arg1
        final String pathToListenForEventsIn = "testchannel"; // arg3
        final String midiDeviceName = "toReason"; // arg4

        final MidiDevice midiDevice
                = IacDeviceUtilities.getMidiReceivingDevice(midiDeviceName);
        final MidiReceiver br = new BufferedMidiReceiver(midiDevice.getReceiver());

        FBMidiReadingEventGenerator midiReadingEventSource
                = new FBMidiReadingEventGenerator(databaseName, configFile, pathToListenForEventsIn, br);

        Thread.currentThread().join();
    }
}
