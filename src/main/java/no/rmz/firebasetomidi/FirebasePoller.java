package no.rmz.firebasetomidi;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.FileInputStream;
import java.io.IOException;
import javax.sound.midi.MidiDevice;
import no.rmz.eventgenerators.EventReceiver;
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
     */
    public final static void main(final String[] argv) throws SequencerException,
            IOException, InterruptedException, JitterPreventionFailureException {

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

        seq = PlingPlongSequencer.newBuilder()
                .setDevice(midiDevice)
                .setSignalSource(midiReadingEventSource)
                .setSoundGenerator(new OneNoteSoundGenerator())
                .build();
        seq.start();
        Thread.currentThread().join();
    }
}
