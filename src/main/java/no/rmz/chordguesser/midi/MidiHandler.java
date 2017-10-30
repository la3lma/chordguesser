package no.rmz.chordguesser.midi;

import static com.google.common.base.Preconditions.*;
import java.util.List;
import java.util.logging.*;
import javax.sound.midi.*;
import no.rmz.chordguesser.NoteListener;

public final class MidiHandler {

    private final static Logger LOG = Logger.getLogger(MidiHandler.class.getName());
    private final NoteListener listener;

    public MidiHandler(final NoteListener listener) {
        checkNotNull(listener, "The MidiHandler must have a ToneListener instance");
        this.listener = listener;
    }

    public void run() {
        wireMidiTransmitters();
    }

    private void wireMidiTransmitters() {
        // Pick up a vector of info from the midi subsystem, then
        // iterate over all the info elements.

        final MidiDevice.Info[] midiInfoVector;
        midiInfoVector = MidiSystem.getMidiDeviceInfo();

        for (int i = 0; i < midiInfoVector.length; i++) {
            try {
                final MidiDevice device = MidiSystem.getMidiDevice(midiInfoVector[i]);
                final String description = device.getDeviceInfo().toString();
                final Receiver receiver = new MidiInputReceiver(description, listener);

                // XXX This is cargo cult programming, and it doesn't work!
                //does the device have any transmitters?
                //if it does, add it to the device list
                final MidiDevice.Info info = midiInfoVector[i];

                LOG.log(Level.INFO, "i = {0}, Processing: {1}, name = {2}, vendor {3}, version {4}",
                        new Object[]{i, info.getDescription(), info.getName(), info.getVendor(), info.getVersion()});

                final List<Transmitter> transmitters = device.getTransmitters();

                transmitters.stream().forEach((transmitter) -> {
                    transmitter.setReceiver(receiver);
                });

                final Transmitter trans = device.getTransmitter();
                int maxTransmitters = device.getMaxTransmitters();
                if ((maxTransmitters > transmitters.size())
                        || maxTransmitters == -1) {
                    if (trans.getReceiver() != receiver) {
                        trans.setReceiver(receiver);
                        device.open();
                    }
                }

                // if code gets this far without throwing an exception
                // print a success message
                LOG.log(Level.INFO, "{0} Was Opened", midiInfoVector[i].getDescription());

            } catch (MidiUnavailableException e) {
                LOG.log(Level.INFO,
                        "Couldn''t connect with detected  device {0}", midiInfoVector[i]);
            }
        }
    }
}
