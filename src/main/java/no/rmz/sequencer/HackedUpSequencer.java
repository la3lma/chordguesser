package no.rmz.sequencer;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

public class HackedUpSequencer {

    private final static String MIDIFILENAME ="Disney_Themes_-_Fantasmic.mid";

    private final String name;
    private final AtomicBoolean isRunning;
    private final Runnable runnable;
    private final Executor executor;
    private final MidiDevice device;
    private final Receiver rcvr;
    final ShortMessage myMsg;
    private Transmitter xmit;
    private Receiver receiver;

    public HackedUpSequencer(final MidiDevice device) {

        checkNotNull(device);
        this.device = device;
        this.executor = Executors.newSingleThreadExecutor();
        this.isRunning = new AtomicBoolean(false);
        this.name = device.getDeviceInfo().getDescription();
        int noOfReceivers = device.getMaxReceivers();
        int noOfTransmitters = device.getMaxTransmitters();

        List<Receiver> receivers = device.getReceivers();
        List<Transmitter> transmitters = device.getTransmitters();
        try {
            device.open();
            this.rcvr = device.getReceiver();
        } catch (MidiUnavailableException ex) {
            throw new IllegalStateException(name + " MIDI receiver unavailable", ex);
        }

        if (noOfTransmitters != 0){
            try {
                this.receiver = new Receiver() {

                    @Override
                    public void send(MidiMessage message, long timeStamp) {
                       System.out.println("   -> " + name + " received " + message.toString());
                    }

                    @Override
                    public void close() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                };
                System.out.println(" ==> Adding a receiver for " + name);
                this.xmit = device.getTransmitter();
                this.xmit.setReceiver(this.receiver);
            } catch (MidiUnavailableException ex) {
                 throw new IllegalStateException(name + " MIDI transmitter unavailable", ex);
            }
        } else {
            this.xmit = null;
        }
        this.myMsg = new ShortMessage();

        try {
            // Start playing the note Middle C (60),
            // moderately loud (velocity = 93).
            myMsg.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
        } catch (InvalidMidiDataException ex) {
            throw new IllegalStateException( name + " couldn't make message", ex);
        }

        this.runnable = () -> {
            while (isRunning.get()) {
                sequenceSomething();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    stop();
                    throw new RuntimeException(ex);
                }
            }
        };
    }

    public final static void startSequencer(final MidiDevice device) {
        final String  deviceInfo = device.getDeviceInfo().getName();

        if ("Bus 1".equals(deviceInfo)) {
            final HackedUpSequencer hackedUpSequencer
                    = new HackedUpSequencer(device);
            // Fire up a thread and make it output something.
            hackedUpSequencer.start();

            try {

                Sequencer sequencer =
                // Get default sequencer.

                sequencer = MidiSystem.getSequencer();

                if (sequencer == null) {
                     // Error -- sequencer device is not supported.
                    // Inform user and return...
                    throw new RuntimeException("Device not supported");
                } else {
                    // Acquire resources and make operational.
                    sequencer.open();
                }

                File myMidiFile = new File(MIDIFILENAME);
                // Construct a Sequence object, and
                // load it into my sequencer.
                Sequence mySeq = MidiSystem.getSequence(myMidiFile);
                sequencer.setSequence(mySeq);
                sequencer.getTransmitter().setReceiver(device.getReceiver());
               //  sequencer.start();  // XXX This is a really horrible song :-)
            } catch (Exception e) {
                // Handle error and/or return
                throw new RuntimeException(e);
            }


        }
    }

    private final AtomicInteger seqNo = new AtomicInteger(1);

    private void sequenceSomething() {

        final long timeStamp = -1;

        try {rcvr.send(myMsg, timeStamp);

            System.out.println(name + " sequenced something " + seqNo.addAndGet(1));
        } catch (Exception e) {
            System.out.println(name + " couldn't send " + e);
            stop();
        }
    }

    public void start() {
        synchronized (isRunning) {
            if (!isRunning.get()) {
                isRunning.getAndSet(true);
                executor.execute(runnable);
            } else {
                throw new IllegalStateException(
                        "Attempt to start already running sequencer");
            }
        }
    }


     public void stop() {
        synchronized (isRunning) {
            if (isRunning.get()) {
                isRunning.getAndSet(false);
            } else {
                throw new IllegalStateException(
                        "Attempt to stop an  already stopped sequencer");
            }
        }
    }
}
