package no.rmz.sequencer;

public interface SyncSource {

    public void start();
    
    public void addReceiver(final Runnable runnable);
}
