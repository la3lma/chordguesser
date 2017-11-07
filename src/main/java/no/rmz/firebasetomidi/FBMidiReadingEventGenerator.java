package no.rmz.firebasetomidi;

import com.google.common.base.Preconditions;
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

    public FBMidiReadingEventGenerator(final String databaseName, final String configFile, final String eventpath) {
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
                    // Then send the FbMidiEvent somewhere where can be heard
                    // XXX Send the MIDI event to something running in some other thread.
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
