package no.rmz.firebasetomidi;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import no.rmz.sequencer.EventSource;
import no.rmz.sequencer.IacDeviceUtilities;
import no.rmz.sequencer.PlingPlongSequencer;
import no.rmz.sequencer.SequencerException;
import static org.junit.Assert.fail;
import org.junit.Test;

public class FBMidiReadingEventGeneratorIT {

    public FBMidiReadingEventGeneratorIT() {
    }

    @Test
    public void testSomeMethod() throws SequencerException, InterruptedException, MidiUnavailableException {

        // These should be gotten from the argv
        final String configFile = "fbmidibridge-1746b45f5da7.json";  // arg2
        final String databaseName = "fbmidibridge"; // arg1
        final String pathToListenForEventsIn = "testchannel"; // arg3
        final String midiDeviceName = "toReason"; // arg4

        final MidiDevice midiDevice
                = IacDeviceUtilities.getMidiReceivingDevice(midiDeviceName);

        final Receiver receiver = midiDevice.getReceiver();

        final BufferedMidiReceiver br = new BufferedMidiReceiver(receiver);

        final CountDownLatch latch = new CountDownLatch(1);
        final MidiReceiver latchingReceiver = (ShortMessage msg) -> {
            latch.countDown();
            br.put(msg);
        };

        final PlingPlongSequencer seq;
        final EventSource midiReadingEventSource;
        midiReadingEventSource = new FBMidiReadingEventGenerator(databaseName, configFile, pathToListenForEventsIn, latchingReceiver);

        if (!latch.await(10, TimeUnit.SECONDS)) {
            fail("Couldn't receive sample MIDI message through firebase.  So sad.");
        }
    }
}
