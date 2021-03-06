package com.senacor.tecco.reactive.example.creating;

import org.junit.Test;
import rx.Observable;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class ThrowTest {
    @Test
    public void testThrow() throws Exception {
        Observable.<String>error(new IllegalStateException("not yet implemented"))
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> print("complete!"));
    }
}