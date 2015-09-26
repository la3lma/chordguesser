package no.rmz.execution;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import no.rmz.sequencer.SyncSource;

/**
 * A process that is being run again, and again, and again, until it stops.
 * Highly overengineered class. SHould be killed at first opportunity.
 */
public final class RecurringProcess implements SyncSource {

    private final Executor executor;
    private final Runnable scheduledRunable;
    private final AtomicBoolean isRunning;
    private final long sleepInterval = 500;
    private Collection<Runnable> receivers;

    public RecurringProcess(final Runnable runnable) {
        this();
        checkNotNull(runnable);
        synchronized (receivers) {
            receivers.add(runnable);
        }
    }

    public RecurringProcess() {

        this.receivers = new LinkedList<>();
        this.executor = Executors.newSingleThreadExecutor();
        this.isRunning = new AtomicBoolean(false);
        //  Doing the actual sequencing.
        this.scheduledRunable = new Runnable() {

            @Override
            public void run() {
                while (isRunning.get()) {
                    synchronized (receivers) {
                        for (final Runnable r : receivers) {
                            r.run();
                        }
                    }
                    try {
                        Thread.sleep(sleepInterval);
                    } catch (InterruptedException ex) {
                        stop();
                        throw new RuntimeException(ex);
                    }
                }
            }
        };
    }

 

    @Override
    public void start() {
        synchronized (isRunning) {
            if (!isRunning.get()) {
                isRunning.getAndSet(true);
                executor.execute(scheduledRunable);
            } else {
                throw new IllegalStateException("Attempt to start already running sequencer");
            }
        }
    }

    public void stop() {
        synchronized (isRunning) {
            if (isRunning.get()) {
                isRunning.getAndSet(false);
            } else {
                throw new IllegalStateException("Attempt to stop an  already stopped sequencer");
            }
        }
    }

    @Override
    public void addReceiver(final Runnable runnable) {
        receivers.add(runnable);
    }
}
