package no.rmz.chordguesser;

import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import static com.google.common.base.Preconditions.*;

public final class MidiHandler {

    private final ToneListener listener;
    
    public MidiHandler(final ToneListener listener) {
        checkNotNull(listener, "The MidiHandler must have a ToneListener instance");
        this.listener = listener;
    }

    public void run() {

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            try {
                final MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
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
                            new MidiInputReceiver(device.getDeviceInfo().toString()));
                }

                final Transmitter trans = device.getTransmitter();
                trans.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));

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

    // XXX See http://www.ibm.com/developerworks/library/it/it-0801art38/
    //     http://www.gweep.net/~prefect/eng/reference/protocol/midispec.html
    //     https://ccrma.stanford.edu/~craig/articles/linuxmidi/misc/essenmidi.html
    public final class MidiInputReceiver implements Receiver {

        public String name;

        public MidiInputReceiver(final String name) {
            this.name = name;
        }

        @Override
        public void send(final MidiMessage msg, final long timeStamp) {
            System.out.println("midi received " + msg.toString());
            final byte[] m = msg.getMessage();
            System.out.printf("%02X%02X%02X%02X\n",
                    m[0], m[1], m[2], (m.length == 4) ? m[3] : 0);
            
            // XXX Detect tone on and tone off and send those down the line,
            //     ignore everything else.
        }

        @Override
        public void close() {
        }
    }
}
