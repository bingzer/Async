package com.bingzer.android.async.handlers;

import android.test.AndroidTestCase;

import com.bingzer.android.async.Task;

import java.util.concurrent.CountDownLatch;

import static com.bingzer.android.async.Async.doAsync;

public class DelegateHandlerTest extends AndroidTestCase {
    int counter = 0;

    @Override
    protected void setUp() throws Exception {
        counter = 0;
    }

    public void testAsync() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        getPayloadAsync(new Task<String>() {
            @Override
            public void onCompleted(String result) {
                ++counter;
                signal.countDown();
                assertEquals("payload", result);
            }
        });

        signal.await();

        assertEquals(1, counter);
    }

    public void testAsyncThrow_WithErrorReporting() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        // we use Task.WithErrorReporting (should report the error)
        throwPayloadAsync(new Task.WithErrorReporting<String>() {
            @Override
            public void onCompleted(String result) {
                fail("Should throw!");
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

    private void getPayloadAsync(Task<String> task){
        doAsync(new DelegateHandler<String>(task) {
            @Override
            public String invokeOrThrow() throws Throwable {
                return getPayload();
            }
        });
    }

    private String getPayload(){
        return "payload";
    }

    private void throwPayloadAsync(Task<String> task){
        doAsync(new DelegateHandler<String>(task) {
            @Override
            public String invokeOrThrow() throws Throwable {
                return throwPayload();
            }
        });
    }

    private String throwPayload(){
        throw new RuntimeException("The fault in our stars");
    }
}
