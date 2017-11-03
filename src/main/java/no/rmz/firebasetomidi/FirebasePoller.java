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

    public final static class FBMidiReadingEventGenerator implements EventSource {

        private static final Logger LOG = LoggerFactory.getLogger(FBMidiReadingEventGenerator.class);


        private String configFile;
        private String databaseName;
        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference midiInputMessages;
        private final String eventpath;
        private final EventExecutor eventExecutor;

        public FBMidiReadingEventGenerator(
                final String databaseName,
                final String configFile,
                final String eventpath) {
            this.configFile = checkNotNull(configFile);
            this.databaseName = checkNotNull(databaseName);
            this.eventpath = checkNotNull(eventpath);
            this.eventExecutor = new EventExecutor();

            try (final FileInputStream serviceAccount = new FileInputStream(configFile)) {

                final FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                        .setDatabaseUrl("https://" + databaseName + ".firebaseio.com/")
                        .build();

                try {
                    FirebaseApp.getInstance();
                } catch (Exception e) {
                    FirebaseApp.initializeApp(options);
                }

                this.firebaseDatabase = FirebaseDatabase.getInstance();

                // (un)comment next line to turn on/of extended debugging
                // from firebase.
                // this.firebaseDatabase.setLogLevel(com.google.firebase.database.Logger.Level.DEBUG);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            // XXX Read this, then fix something that reports connectivity status through the
            //     health mechanism.
            // https://www.firebase.com/docs/web/guide/offline-capabilities.html#section-connection-state
            // this.firebaseDatabase.getReference("/.info/connected").addValueEventListener()
            this.midiInputMessages = firebaseDatabase.getReference(eventpath);

            final ValueEventListener productCatalogValueEventListener
                    = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    LOG.info("onDataChange");
                    // XXX TBD
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    LOG.info("onDataChange");
                }
            };

            this.midiInputMessages.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                    LOG.info("onChildAdded");
                    if (snapshot == null) {
                        LOG.error("dataSnapshot can't be null");
                        return;
                    }

                    if (!snapshot.exists()) {
                        LOG.error("dataSnapshot must exist");
                        return;
                    }

                    try {
                        final FBMidiEvent midiEvent
                                = snapshot.getValue(FBMidiEvent.class);
                        // XXX Send the MIDI event to something running in some other thread.
                    } catch (Exception e) {
                        LOG.error("Couldn't transform req into FbPurchaseRequest", e);
                    }
                }

                // The methods below are added just to
                // fulfill the interface contract, they don't actually
                // do anything.
                @Override
                public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                }

                @Override
                public void onChildRemoved(DataSnapshot snapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }

        @Override
        public void start() {
            // DO we need to start anything?
        }

        @Override
        public void addReceiver(final EventReceiver receiver) {
            eventExecutor.addMidiEventListener((FbMidiEventListener) receiver);
        }
    }

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
