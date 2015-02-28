package com.bingzer.android.async;

import android.test.AndroidTestCase;

import com.bingzer.android.async.handlers.VoidHandler;

import java.util.concurrent.CountDownLatch;

import static com.bingzer.android.async.Async.doAsync;

/**
 * Ricky Tobing (2015)
 */
public class TaskWithErrorReporting extends AndroidTestCase {

    int counter = 0;

    @Override
    protected void setUp() throws Exception {
        counter = 0;
    }

    public void testSomething() throws Exception{
        final CountDownLatch signal = new CountDownLatch(1);

        throwSomethingAsync(new Task.WithErrorReporting<Float>() {
            @Override
            public void onError(Throwable error) {
                ++counter;
                signal.countDown();
            }

            @Override
            public void onCompleted(Float result) {
                fail("Should go thru onError() instead");
            }
        });

        signal.await();
        assertEquals(1, counter);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private void throwSomething(){
        throw new RuntimeException("Something");
    }

    private void throwSomethingAsync(Task task){
        doAsync(new VoidHandler(task) {
            @Override public void invokeOrThrow() throws Throwable {
                throwSomething();
            }
        });
    }
}
