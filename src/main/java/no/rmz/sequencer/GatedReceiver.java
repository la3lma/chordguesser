package no.rmz.sequencer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;
import no.rmz.eventgenerators.EventDistributor;
import no.rmz.eventgenerators.EventReceiver;
import no.rmz.eventgenerators.ParsedEvent;

/**
 * Will collect input.  If when polled the
 * number of inputs are above the threshold,
 * true or a number indicating the number of events
 * seen since last reading.
 */
public final class GatedReceiver  implements EventReceiver {
    
    private final AtomicInteger count;
    private final IntUnaryOperator setToZero;
    private final EventDistributor eventDistributor;
    
    public GatedReceiver() {
        count = new AtomicInteger(0);
        setToZero = (int operand) -> 0; 
        this.eventDistributor = new EventDistributor();
    }
    
    private void inputEvent() {
        count.addAndGet(1);
    }
    
    /**
     * Returns true iff any events has arrived since last poll. Sets number
     * of events to zero after reading.
     * @return true iff events have arrived.
     */
    public boolean eventsHaveArrivedSinceLastPoll() {
        return noOfEventsSinceLastPoll() > 0;
    }
    
    /**
     * Returns  number of events that has arrived since last poll. Sets number
     * of events to zero after reading.
     * @return  number of events
     */
    public int noOfEventsSinceLastPoll() {
        return count.getAndUpdate(setToZero); 
    }  

    @Override
    public void receive(final ParsedEvent event) {
        inputEvent();
    }
   
}
