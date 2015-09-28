package no.rmz.eventgenerators;

import static com.google.common.base.Preconditions.checkNotNull;
import no.rmz.sequencer.EventSource;

/**
 * A process that is being run again, and again, and again, until it stops.
 */
public final class PingEveryHalfSecond implements EventSource {

    private final EventDistributor broadcaster;
    private final PeriodicInvoker pv;

    public PingEveryHalfSecond(final Runnable runnable) {
        this();
        checkNotNull(runnable);
        broadcaster.add(runnable);
    }

    public PingEveryHalfSecond() {
        this.broadcaster = new EventDistributor();
        this.pv = new PeriodicInvoker(new Runnable() {

            @Override
            public void run() {
                broadcaster.broadcast();
            }
        });
    }

    @Override
    public void addReceiver(final Runnable runnable) {
        broadcaster.add(runnable);
    }

    @Override
    public void start() {
        pv.start();
    }
}
