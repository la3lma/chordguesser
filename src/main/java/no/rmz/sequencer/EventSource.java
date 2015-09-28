package no.rmz.sequencer;

public interface EventSource {

    public void start();
    
    public void addReceiver(final Runnable runnable);
}
