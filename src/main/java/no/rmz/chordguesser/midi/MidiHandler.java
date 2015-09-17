package no.rmz.chordguesser.midi;

import no.rmz.sequencer.PlingPlongSequencer;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiUnavailableException;

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

                // XXX Awful hack
                if ("javaSequencer Bus 1".equals(info.getDescription())) {
                    System.out.println("--->Now looking at javaSequencer bus1");
                    if (device.getMaxReceivers() > 0) {
                        System.out.println("Found receivers in  bus1");
                        PlingPlongSequencer.startSequencer(device);
                    } else {
                        System.out.println("Found no receivers in bus1");
                    }
                }

                System.out.flush();
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

                //if code gets this far without throwing an exception
                //print a success message
                System.out.println(infos[i].getDescription() + " Was Opened");

            } catch (MidiUnavailableException e) {
                LOG.log(Level.INFO,
                        "Couldn''t connect with detected  device {0}", infos[i]);
            }
        }
    }

    

}
