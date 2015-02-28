package com.bingzer.android.async;

import android.test.AndroidTestCase;

import java.util.concurrent.CountDownLatch;

import static com.bingzer.android.async.Async.doAsync;

public class AsyncTest extends AndroidTestCase {
    int counter = 0;

    public void test_doAsync() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        doAsync(new Task<Object>() {
            @Override
            public void onCompleted(Object result) {
                ++counter;
                signal.countDown();
            }
        }, new Delegate<Object>() {
            @Override
            public Object invoke() {
                return null;
            }
        });

        signal.await();

        assertTrue(counter == 1);
    }
}
