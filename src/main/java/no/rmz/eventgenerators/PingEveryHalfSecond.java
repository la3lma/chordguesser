package no.rmz.eventgenerators;

import static com.google.common.base.Preconditions.checkNotNull;
import no.rmz.sequencer.EventSource;

/**
 * A process that is being run again, and again, and again, until it stops.
 */
public final class PingEveryHalfSecond implements EventSource {

    private final EventDistributor broadcaster;
    private final PeriodicInvoker pv;

    public PingEveryHalfSecond(final EventReceiver receiver) {
        this();
        checkNotNull(receiver);
        broadcaster.add(receiver);
    }

    public PingEveryHalfSecond() {
        this.broadcaster = new EventDistributor();
        this.pv = new PeriodicInvoker(new Runnable() {

            @Override
            public void run() {
                broadcaster.broadcast(new ParsedEvent() {

                    @Override
                    public boolean isValid() {
                        return true;
                    }

                    @Override
                    public long getTimestamp() {
                        return -1;
                    }
                });
            }
        });
    }

    @Override
    public void addReceiver(final EventReceiver rec) {
        broadcaster.add(rec);
    }

    @Override
    public void start() {
        pv.start();
    }
}
