package no.rmz.eventgenerators;

import java.util.Collection;
import java.util.LinkedList;


public  final class EventDistributor {
    private Collection<Runnable> receivers;

    public EventDistributor() {
        this.receivers = new LinkedList<>();
    }

    public void broadcast() {
        synchronized (receivers) {
            for (final Runnable r : receivers) {
                r.run();
            }
        }
    }

    void add(Runnable runnable) {
        synchronized (receivers) {
            receivers.add(runnable);
        }
    }
}
