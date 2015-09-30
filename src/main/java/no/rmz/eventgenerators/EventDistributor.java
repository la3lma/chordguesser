package no.rmz.eventgenerators;

import java.util.Collection;
import java.util.LinkedList;


public  final class EventDistributor {
    private final Collection<EventReceiver> receivers;

    public EventDistributor() {
        this.receivers = new LinkedList<>();
    }

    public void broadcast(final ParsedEvent event) {
        synchronized (receivers) {
            for (final EventReceiver r : receivers) {
                r.receive(event);
            }
        }
    }

    void add(EventReceiver runnable) {
        synchronized (receivers) {
            receivers.add(runnable);
        }
    }
}
