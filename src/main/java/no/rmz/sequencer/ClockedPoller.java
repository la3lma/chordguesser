package no.rmz.sequencer;

import no.rmz.eventgenerators.EventDistributor;
import no.rmz.eventgenerators.EventReceiver;

/**
 * Follows a beat, and on regular intervals polls a gated receiver
 * to see if it has any output.  Based on that output a signal is
 * made or not.
 */
public class ClockedPoller implements EventSource{
    final GatedReceiver gr;
    final int millisToSleep;
    final EventDistributor distributor;
    

    public ClockedPoller(final GatedReceiver gr, final int millis) {
        this.gr = gr;
        this.millisToSleep = millis;
        this.distributor = new EventDistributor();
    }

    @Override
    public void start() {
        // XXX Use an execution service instead!
        final Thread thread = new Thread(() -> {
            while(true) {
                if (gr.eventsHaveArrivedSinceLastPoll()) {
                    distributor.broadcast(null);
                }
                try {
                    Thread.sleep(millisToSleep);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        thread.setName("Clocked poller");
        thread.start();
    }

    @Override
    public void addReceiver(EventReceiver receiver) {
        distributor.add(receiver);
    } 
}
