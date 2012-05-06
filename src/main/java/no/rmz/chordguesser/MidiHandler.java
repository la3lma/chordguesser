package no.rmz.chordguesser;

import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiUnavailableException;
import static com.google.common.base.Preconditions.*;

public final class MidiHandler {

    private final NoteListener listener;

    public MidiHandler(final NoteListener listener) {
        checkNotNull(listener, "The MidiHandler must have a ToneListener instance");
        this.listener = listener;
    }

    public void run() {

        // Pick up a vector of info from the midi subsystem, then
        // iterate over all the info elements.
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            try {


                final MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
                final String description = device.getDeviceInfo().toString();


                //does the device have any transmitters?
                //if it does, add it to the device list
                System.out.println(infos[i]);

                //get all transmitters
                final List<Transmitter> transmitters = device.getTransmitters();
                //and for each transmitter

                for (int j = 0; j < transmitters.size(); j++) {
                    //create a new receiver
                    transmitters.get(j).setReceiver(
                            //using my own MidiInputReceiver
                            new MidiInputReceiver(description, listener));
                }

                final Transmitter trans = device.getTransmitter();
                trans.setReceiver(new MidiInputReceiver(description, listener));

                //open each device
                device.open();
                //if code gets this far without throwing an exception
                //print a success message
                System.out.println(device.getDeviceInfo() + " Was Opened");


            } catch (MidiUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
