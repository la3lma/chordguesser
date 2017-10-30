package no.rmz.sequencer;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import no.rmz.scales.ScaleBean;


public final class RandomScaleToneGenerator implements SoundGenerator {
    private final List<ShortMessage> myMsgs;
    private final ScaleBean myScale;
    
    
   public final class MessageHolder implements Comparable<MessageHolder> {
       private final long timestamp;
       private ShortMessage msg;

        public MessageHolder(final long timestamp, final ShortMessage msg) {
            if (timestamp < 0 ) {
                throw new IllegalArgumentException(
                        "negative timestamp " + timestamp);
            }
            this.timestamp = timestamp;
            this.msg = checkNotNull(msg);
        }

        @Override
        public int compareTo(final MessageHolder o) {
            checkNotNull(o);
            return Long.compare(timestamp, o.timestamp);
        }
   }
   
   // XXX Make a new clocked sequence to ensure that things happen in order.
   public final class ClockedSequencer {
       private final TreeSet<MessageHolder> messages = new TreeSet<>();
       
       private final ExecutorService es;

        public ClockedSequencer() {
            this.es = Executors.newSingleThreadExecutor();
            this.es.submit(new Runnable() {

                @Override
                public void run() {
                    
                }
            });
        }
       
       
       
       public void add(final MessageHolder mh) {
           messages.add(mh);
       }
   }

    public RandomScaleToneGenerator(final ScaleBean scale) {
        this.myScale = checkNotNull(scale);
        this.myMsgs = new LinkedList<>();
        try {
            scale.getBinary12notes();
            final String scaletones = scale.getBinary12notes();
            for (int i = 0; i < scaletones.length(); i++) {
                if (scaletones.charAt(i) == '1') {
                    final ShortMessage onMessage = new ShortMessage();
                    onMessage.setMessage(ShortMessage.NOTE_ON, 0, 60 + i, 93);
                    myMsgs.add(onMessage);
                }
            }
        } catch (InvalidMidiDataException ex) {
            throw new IllegalStateException(" couldn't make message", ex);
        }
    }

    @Override
    public void generate(final Receiver recv) {
        try {
            final long timeStamp =   -1; // XXX Perhaps use this?
            final ShortMessage myMsg = randomScaleTone();
            recv.send(myMsg, timeStamp);
            final ShortMessage endMessage = new ShortMessage();
            endMessage.setMessage(ShortMessage.NOTE_OFF, 0, myMsg.getData1(), 0);
        } catch (InvalidMidiDataException ex) {
            throw new RuntimeException(ex);
        }
    }

    private ShortMessage randomScaleTone() {
        final double random = Math.random();
        final double scale  = random * myMsgs.size();
        final int    note = (int) Math.round(scale) % myMsgs.size();
        return myMsgs.get(note);
    }
}
