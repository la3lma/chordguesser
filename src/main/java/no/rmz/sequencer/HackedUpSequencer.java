package no.rmz.sequencer;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sound.midi.MidiDevice;

public class HackedUpSequencer {
    
    private final AtomicBoolean isRunning;
    private final Runnable runnable ;
    private final Executor executor;
    private final MidiDevice device;

    public HackedUpSequencer(final MidiDevice device) {
        checkNotNull(device);
        this.device = device;
        this.executor = Executors.newSingleThreadExecutor();
        this.isRunning = new AtomicBoolean(false);

        this.runnable = () -> {
            while (isRunning.get()) {
                sequenceSomething();
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

        };
    }

    private void sequenceSomething() {
        System.out.println("Sequencing something");
    }

    public void start() {
        synchronized (isRunning) {
            if (!isRunning.get()) {
                isRunning.getAndSet(true);
                executor.execute(runnable);
            } else {
                throw new IllegalStateException(
                        "Attempt to start already running sequencer");
            }
        }
    }
}
