package no.rmz.sequencer;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A process that is being run again, and again, and again, until it stops.
 */
public final  class RecurringProcess {
    private final Executor executor;
    private final Runnable scheduledRunable;
    private final AtomicBoolean isRunning;
    private long sleepInterval = 3000;

    public RecurringProcess(final Runnable runnable) {
        Preconditions.checkNotNull(runnable);
        this.executor = Executors.newSingleThreadExecutor();
        this.isRunning = new AtomicBoolean(false);
        //  Doing the actual sequencing.
        this.scheduledRunable = () -> {
            while (isRunning.get()) {
                runnable.run();
                try {
                    Thread.sleep(sleepInterval);
                } catch (InterruptedException ex) {
                    stop();
                    throw new RuntimeException(ex);
                }
            }
        };
    } //  Doing the actual sequencing.

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
    
}
