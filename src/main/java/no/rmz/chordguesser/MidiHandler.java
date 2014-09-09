package no.rmz.chordguesser;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiUnavailableException;
import static com.google.common.base.Preconditions.*;
import java.util.List;
import java.util.logging.Logger;
import javax.sound.midi.*;

public final class MidiHandler {

    private final static Logger LOG = Logger.getLogger(MidiHandler.class.getName());
    private final NoteListener listener;

    public MidiHandler(final NoteListener listener) {
        checkNotNull(listener, "The MidiHandler must have a ToneListener instance");
        this.listener = listener;
    }

    public void run() {

        // Pick up a vector of info from the midi subsystem, then
        // iterate over all the info elements.

        final MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            try {
                final MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
                final String description = device.getDeviceInfo().toString();
                final Receiver receiver = new MidiInputReceiver(description, listener);

                // XXX This is cargo cult programming, and it doesn't work!
                //does the device have any transmitters?
                //if it does, add it to the device list
                final MidiDevice.Info info = infos[i];
                System.out.println("i = " + i
                        + ", Processing: " + info.getDescription()
                        + ", name = " + info.getName()
                        + ", vendor " + info.getVendor()
                        + ", version " + info.getVersion());
                System.out.flush();
                final List<Transmitter> transmitters = device.getTransmitters();
                final List<Receiver> receivers = device.getReceivers();

                for (final Transmitter transmitter : transmitters) {
                    //create a new receiver
                    transmitter.setReceiver(receiver);
                }

                final Transmitter trans = device.getTransmitter();
                int maxTransmitters = device.getMaxTransmitters();
                if ((maxTransmitters > transmitters.size()) || maxTransmitters == -1) {
                    if (trans.getReceiver() != receiver) {
                        trans.setReceiver(receiver);
                        device.open();
                    }
                }

                //if code gets this far without throwing an exception
                //print a success message
                System.out.println(infos[i].getDescription() + " Was Opened");

            } catch (MidiUnavailableException e) {
                LOG.info("Couldn't connect with detected  device " + infos[i]);
            }
        }
    }
}
