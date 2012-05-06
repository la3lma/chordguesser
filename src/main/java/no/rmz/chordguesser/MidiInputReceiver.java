package no.rmz.chordguesser;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/**
 * 
 */
public final class MidiInputReceiver implements Receiver {
    // XXX See http://www.ibm.com/developerworks/library/it/it-0801art38/
    //     http://www.gweep.net/~prefect/eng/reference/protocol/midispec.html
    //     https://ccrma.stanford.edu/~craig/articles/linuxmidi/misc/essenmidi.html
    //     http://midi.songstuff.com/article/midi_message_format
    //     http://www.gweep.net/~prefect/eng/reference/protocol/midispec.html
    
    private final String name;
    private final NoteListener listener;
    private final MidiMessageDecoder mmd;

     public MidiInputReceiver(final String name, final NoteListener listener) {
         // XXX Check for nulls.
        this.name = name;
        this.listener = listener;
        this.mmd = new MidiMessageDecoder(listener); 
    }

  

    @Override
    public void send(final MidiMessage msg, final long timeStamp) {
        System.out.println("midi received " + msg.toString());
        final byte[] m = msg.getMessage();
        
        
        System.out.printf("%02X%02X%02X%02X\n", m[0], m[1], m[2], (m.length == 4) ? m[3] : 0);
        // XXX Detect tone on and tone off and send those down the line,
        //     ignore everything else.
        mmd.decode(m);
       
    }

    @Override
    public void close() {
    }
    
}
