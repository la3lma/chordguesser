package no.rmz.sequencer;

import com.sun.media.sound.AudioSynthesizer;
import com.sun.media.sound.SoftSynthesizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

public class PlayOneNote {

    /*
    public static void baz() {
        Receiver recv;
        ShortMessage sm;
        try (AudioSynthesizer synth = new SoftSynthesizer()) {
            recv = synth.getReceiver();
            assertTrue(recv != null);
            sm = new ShortMessage();
            sm.setMessage(ShortMessage.NOTE_OFF, 0, 64, 64);
            synth.open(new DummySourceDataLine(), null);
            recv.send(sm, -1);
        } catch (MidiUnavailableException ex) {
            LOG.log(Level.SEVERE, "Screqup", ex);
        }
        try {
            recv.send(sm, -1);
            throw new RuntimeException("Exception not thrown!");
       
    }
    */
    private static final Logger LOG = Logger.getLogger(PlayOneNote.class.getName());

    public static void foo() {
        boolean isFailed = false;
        Receiver rcvr;
        ShortMessage shMsg = new ShortMessage();

        try {
            rcvr = MidiSystem.getReceiver();

            shMsg.setMessage(ShortMessage.NOTE_ON, 0, 60, 127);
            rcvr.send(shMsg, -1);
            rcvr.close();
        } catch (MidiUnavailableException e) {
            System.out.println("Midi unavailable, cannot test.");
            return;
        } catch (InvalidMidiDataException ine) {
            System.out.println("InvalidMidiDataException, cannot test.");
            return;
        } catch (IllegalStateException ilEx) {
            ilEx.printStackTrace(System.out);
            System.out.println("IllegalStateException was thrown incorrectly!");
        }
    }

    public final static void main(final String argv[]) {
        bar();
    }

    public static Synthesizer newSynthesizer() throws MidiUnavailableException {
        final AudioSynthesizer synth;
        synth = new SoftSynthesizer();
        synth.open();
        if (!synth.isOpen()) {
            throw new IllegalStateException("synth not open");
        }
        return synth;
    }
    
    private static void bar() {
        try {
            final ShortMessage myMsg = new ShortMessage();
            // Play the note Middle C (60) moderately loud
            // (velocity = 93)on channel 4 (zero-based).
            myMsg.setMessage(ShortMessage.NOTE_ON, 4, 60, 93);
            final Synthesizer synth;
            synth = newSynthesizer();
            
            // Synthesizer synth = MidiSystem.getSynthesizer();
            Receiver synthRcvr = synth.getReceiver();
            synthRcvr.send(myMsg, -1); // -1 means no time stamp
        } catch (InvalidMidiDataException | MidiUnavailableException ex) {
            LOG.log(Level.SEVERE, "screwed up", ex);
        }
        System.out.println("Done");
    }
}
