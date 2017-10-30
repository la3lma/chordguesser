package no.rmz.scheduler;

import com.google.common.base.Preconditions;


final class ScheduledEntry implements Comparable<ScheduledEntry>, Runnable{
    private final long timeInMillis;
    private final Runnable runnable;

    public ScheduledEntry(final long timeInMillis, final Runnable runnable) {
        this.timeInMillis = timeInMillis;
        if (timeInMillis < 0) {
            throw new IllegalArgumentException("Timestamp < zero");
        }
        this.runnable = Preconditions.checkNotNull(runnable);
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    @Override
    public void run() {
         runnable.run();
    }

    @Override
    public int compareTo(ScheduledEntry o) {
        return Long.compare(timeInMillis, o.timeInMillis);
    } 
}
