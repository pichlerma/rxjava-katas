package com.senacor.tecco.reactive.example.filtering;

import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class SampleTest {

    @Test
    public void testSample() throws Exception {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(3000, TimeUnit.MILLISECONDS)
                .sample(1000, TimeUnit.MILLISECONDS)
                .toBlocking()
                .forEach(next -> print("next: %s", next));
    }
}
