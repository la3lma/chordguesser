package no.rmz.firebasetomidi;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public final class EventExecutor {

    private final ThreadFactory tf;
    private final ExecutorService executor;
    private final Object monitor = new Object();
    private final Set<FbMidiEventListener> midiEventListener;

    public EventExecutor() {
        this.tf = new ThreadProducer();
        this.executor = Executors.newCachedThreadPool(tf);
        this.midiEventListener = new HashSet<>();
    }

    public void addMidiEventListener(FbMidiEventListener listener) {
        synchronized (monitor) {
            midiEventListener.add(listener);
        }
    }

    private final class ThreadProducer implements ThreadFactory {

        private final ThreadFactory tf = Executors.defaultThreadFactory();

        @Override
        public Thread newThread(Runnable r) {
            Thread t = tf.newThread(r);
            t.setName("EventExecutor");
            return t;
        }
    }

    public void onMidiEvent(final FBMidiEvent event) {
        synchronized (monitor) {
            midiEventListener.forEach((no.rmz.firebasetomidi.FbMidiEventListener l) -> {
                executor.execute(() -> {
                    l.onFbMidiEvent(event);
                });
            });
        }
    }
}
