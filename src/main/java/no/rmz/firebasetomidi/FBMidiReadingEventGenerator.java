package no.rmz.firebasetomidi;

import com.google.common.base.Preconditions;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.FileInputStream;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import no.rmz.chordguesser.midi.MidiCmd;
import no.rmz.eventgenerators.EventReceiver;
import no.rmz.sequencer.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FBMidiReadingEventGenerator implements EventSource {

    private static final Logger LOG = LoggerFactory.getLogger(FBMidiReadingEventGenerator.class);
    private String configFile;
    private String databaseName;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference midiInputMessages;
    private final String eventpath;
    private final EventExecutor eventExecutor;

    public static ShortMessage asShortMessage(final FbMidiEventBean bean) {
        checkNotNull(bean);

        final MidiCmd cmd = MidiCmd.valueOf(bean.getCmd());
        final ShortMessage myMsg = new ShortMessage();
        switch (cmd) {
            case NOTE_ON:
                try {
                    myMsg.setMessage(ShortMessage.NOTE_ON, bean.getChan(), bean.getNote(), bean.getStrength());
                } catch (InvalidMidiDataException ex) {
                    throw new IllegalStateException(" couldn't make message", ex);
                }
                return myMsg;

            case NOTE_OFF:
                try {
                    myMsg.setMessage(ShortMessage.NOTE_OFF, bean.getChan(), bean.getNote(), bean.getStrength());
                } catch (InvalidMidiDataException ex) {
                    throw new IllegalStateException(" couldn't make message", ex);
                }
                return myMsg;

            default:
                LOG.info("Received MIDI unknown type of MIDI message: " + bean.toString());
        }
        throw new IllegalArgumentException("Could not produce a valid MIDI message from input");
    }


    public FBMidiReadingEventGenerator(
            final String databaseName,
            final String configFile,
            final String eventpath,
            final MidiReceiver midiReceiver) {
        this.configFile = Preconditions.checkNotNull(configFile);
        this.databaseName = Preconditions.checkNotNull(databaseName);
        this.eventpath = Preconditions.checkNotNull(eventpath);
        this.eventExecutor = new EventExecutor();
        try (final FileInputStream serviceAccount = new FileInputStream(configFile)) {
            final FirebaseOptions options = new FirebaseOptions.Builder().setCredential(FirebaseCredentials.fromCertificate(serviceAccount)).setDatabaseUrl("https://" + databaseName + ".firebaseio.com/").build();
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

        // XXX This value event thing is probably not something we need.
        final ValueEventListener productCatalogValueEventListener = new ValueEventListener() {
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

        this.midiInputMessages.addChildEventListener(new AbstractChildEventListener() {
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
                    final FbMidiEventBean midiEvent = snapshot.getValue(FbMidiEventBean.class);
                    LOG.info("just read midi event bean " + midiEvent);

                    final ShortMessage shortMidiMessage = asShortMessage(midiEvent);

                    LOG.info("Short message =  " + shortMidiMessage);
                    midiReceiver.put(shortMidiMessage);

                    // XXX Missing
                    //  o Send event to midi (in separate thread etc)
                    //  o Get rid message that was just read from Firebase.
                } catch (Exception e) {
                    LOG.error("Couldn't transform req into FbPurchaseRequest", e);
                }
            }
        });
    }

    // (un)comment next line to turn on/of extended debugging
    // from firebase.
    // this.firebaseDatabase.setLogLevel(com.google.firebase.database.Logger.Level.DEBUG);
    // XXX Read this, then fix something that reports connectivity status through the
    //     health mechanism.
    // https://www.firebase.com/docs/web/guide/offline-capabilities.html#section-connection-state
    // this.firebaseDatabase.getReference("/.info/connected").addValueEventListener()
    // XXX TBD
    // XXX Send the MIDI event to something running in some other thread.
    // The methods below are added just to
    // fulfill the interface contract, they don't actually
    // do anything.

    @Override
    public void start() {
        // DO we need to start anything?
    }

    @Override
    public void addReceiver(final EventReceiver receiver) {
        LOG.info("foo");
        eventExecutor.addMidiEventListener((FbMidiEventListener) receiver);
    }
}
