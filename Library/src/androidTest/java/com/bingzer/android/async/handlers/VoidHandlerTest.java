package com.bingzer.android.async.handlers;

import android.test.AndroidTestCase;

import com.bingzer.android.async.Task;

import java.util.concurrent.CountDownLatch;

import static com.bingzer.android.async.Async.doAsync;

/**
 * Ricky Tobing (2015)
 */
public class VoidHandlerTest extends AndroidTestCase {

    int counter = 0;

    @Override
    protected void setUp() throws Exception {
        counter = 0;
    }

    public void testAsync() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        doWorkAsync(new Task() {
            @Override public void onCompleted(Object result) {
                ++counter;
                assertNull(result);
                signal.countDown();
            }
        });

        signal.await();

        assertEquals(1, counter);
    }

    public void testAsyncThrow_WithErrorReporting() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        // we use Task.WithErrorReporting (should report the error)
        throwSomethingAsync(new Task.WithErrorReporting() {
            @Override public void onCompleted(Object result) {
                fail("Should go thru onError()");
            }

            @Override
            public void onError(Throwable error) {
                ++counter;
                signal.countDown();
            }
        });

        signal.await();

        assertEquals(1, counter);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private void doWork(){
        // doing work
    }

    private void doWorkAsync(Task task){
        doAsync(new VoidHandler(task) {
            @Override public void invokeOrThrow() throws Throwable {
                doWork();
            }
        });
    }

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
