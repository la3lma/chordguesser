package no.rmz.scheduler;

import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Scheduler {

    private static final Logger LOG = Logger.getLogger(Scheduler.class.getName());

    private final NavigableSet<ScheduledEntry> entries;
    private final Lock lock = new ReentrantLock();
    private final ExecutorService executor;
     
    public Scheduler() {
        this.entries = new TreeSet<>();
        this.executor = Executors.newFixedThreadPool(3);
        executor.submit(() -> queueRunner());
    }

    private void interruptQueue() {
        synchronized (lock) {
            lock.notify();
        }
    }

    private void queueRunner() {

        while (true) {
            try {
                final long currentTimeMillis;
                final long next;
                final long interval;

                currentTimeMillis = System.currentTimeMillis();
                synchronized (entries) {
                    if (entries.isEmpty()) {
                        next = currentTimeMillis + 1000;
                    } else {
                        next = entries.first().getTimeInMillis();
                    }
                }
                interval = Math.max(0, next - currentTimeMillis);

                if (interval != 0) {
                    try {
                        synchronized (lock) {
                            lock.wait(interval);
                        }
                    } catch (InterruptedException ex) {
                        LOG.info("Scheduler was interrupted");
                    }
                }

                synchronized (entries) {
                    while (!entries.isEmpty()
                            && System.currentTimeMillis() < entries.first().getTimeInMillis()) {
                        final ScheduledEntry first = entries.first();
                        entries.remove(first);
                        this.executor.submit(first);
                    }
                }
            } catch (Throwable t) {
                LOG.log(Level.INFO, "Caught throwable during scheduling", t);
            }
        }
    }

    public void add(
            final long timeInMillis,
            final Runnable runnable) {
        add(new ScheduledEntry(timeInMillis, runnable));
    }

    private void add(final ScheduledEntry entry) {
        synchronized (entries) {
            entries.add(entry);
        }
        interruptQueue();
    }
}
