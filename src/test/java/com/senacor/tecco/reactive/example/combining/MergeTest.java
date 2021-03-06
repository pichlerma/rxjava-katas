package com.senacor.tecco.reactive.example.combining;

import com.senacor.tecco.reactive.WaitMonitor;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class MergeTest {

    @Test
    public void testMergeWith() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "second " + value);

        Subscription subscription = stream1.mergeWith(stream2)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(2500, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }

    @Test
    public void testMerge() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();

        Observable<String> stream1 = Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "first " + value);
        Observable<String> stream2 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "second " + value);
        Observable<String> stream3 = Observable.interval(250, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .map(value -> "3rd " + value);

        Subscription subscription = Observable.merge(stream1, stream2, stream3)
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        monitor::complete);

        monitor.waitFor(2500, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }
}
