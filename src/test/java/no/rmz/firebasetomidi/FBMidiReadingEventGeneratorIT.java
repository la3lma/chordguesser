package no.rmz.firebasetomidi;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.sound.midi.MidiDevice;
import no.rmz.sequencer.EventSource;
import no.rmz.sequencer.IacDeviceUtilities;
import no.rmz.sequencer.PlingPlongSequencer;
import no.rmz.sequencer.SequencerException;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author rmz
 */
public class FBMidiReadingEventGeneratorIT {

    public FBMidiReadingEventGeneratorIT() {
    }

    @Test
    public void testSomeMethod() throws SequencerException, InterruptedException {

        // These should be gotten from the argv
        final String configFile = "fbmidibridge-1746b45f5da7.json";  // arg2
        final String databaseName = "fbmidibridge"; // arg1
        final String pathToListenForEventsIn = "testchannel"; // arg3
        final String midiDeviceName = "toReason"; // arg4

        final MidiDevice midiDevice
                = IacDeviceUtilities.getMidiReceivingDevice(midiDeviceName);

        final PlingPlongSequencer seq;
        final EventSource midiReadingEventSource;
        midiReadingEventSource = new FBMidiReadingEventGenerator(databaseName, configFile, pathToListenForEventsIn);

        final CountDownLatch latch = new CountDownLatch(1);

        FbMidiEventListener fbMidiEventListener = new FbMidiEventListener();

        midiReadingEventSource.addReceiver(fbMidiEventListener);

        if (!latch.await(10, TimeUnit.MINUTES)) {
            fail("Didn't get anything from firebase, so failing the test");
        }
    }
}
