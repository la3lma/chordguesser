package no.rmz.scheduler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;


@Ignore
public class SchedulerTest {

    @Test
    public void testAdd() throws InterruptedException {
        final Scheduler s = new Scheduler();
        final CountDownLatch cl = new CountDownLatch(1);
        s.add(0, () -> cl.countDown());
        assertTrue(cl.await(300, TimeUnit.MILLISECONDS));
    }
}
