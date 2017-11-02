package no.rmz.eventgenerators;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


public final class PeriodicInvoker {
    private final Runnable invoked;
    private final Executor executor;
    private final Runnable scheduledRunable;
    private final AtomicBoolean isRunning;
    private final long sleepInterval = 500;

    public PeriodicInvoker(final Runnable invoked) {
        this.invoked = Preconditions.checkNotNull(invoked);
        this.executor = Executors.newSingleThreadExecutor();
        this.isRunning = new AtomicBoolean(false);
        //  Doing the actual sequencing.
        this.scheduledRunable = () -> {
            while (isRunning.get()) {
                invoked.run();
                try {
                    Thread.sleep(sleepInterval);
                } catch (InterruptedException ex) {
                    stop();
                    throw new RuntimeException(ex);
                }
            }
        };
    }

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
