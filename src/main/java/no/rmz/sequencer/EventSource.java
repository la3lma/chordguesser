package no.rmz.sequencer;

import no.rmz.eventgenerators.EventReceiver;

public interface EventSource {

    public void start();
    
    public void addReceiver(final EventReceiver runnable);
}
