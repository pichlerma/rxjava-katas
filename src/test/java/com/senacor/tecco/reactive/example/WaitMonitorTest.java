package com.senacor.tecco.reactive.example;

import com.senacor.tecco.reactive.WaitMonitor;
import org.junit.Ignore;
import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Andreas Keefer
 */
public class WaitMonitorTest {

    @Test
    public void testTimeout() throws Exception {
        WaitMonitor monitor = new WaitMonitor();
        monitor.waitFor(100, TimeUnit.MILLISECONDS);
        assertFalse(monitor.isComplete());
    }

    @Ignore()
    public void testWaitFor() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        Observable.just("test")
                .subscribeOn(Schedulers.computation())
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete
                );
        monitor.waitFor(100, TimeUnit.MILLISECONDS);
        assertTrue(monitor.isComplete());
    }
}