package com.bingzer.android.async;

import android.test.AndroidTestCase;

import java.util.concurrent.CountDownLatch;

import static com.bingzer.android.async.Async.doAsync;

/**
 * Ricky Tobing (2015)
 */
public class TaskWithProgressReportingTest extends AndroidTestCase {

    int counter = 0;

    @Override
    protected void setUp() throws Exception {
        counter = 0;
    }

    public void testInvoke() throws Exception{
        final CountDownLatch signal = new CountDownLatch(1);

        loadAsync(new Task.WithProgressReporting<Float>() {
            @Override public void onProgress(float current, float total) {
                counter++;
                assertEquals(10f, total);
            }

            @Override public void onError(Throwable error) {
                fail(error.getMessage());
            }

            @Override public void onCompleted(Float result) {
                assertEquals(10f, result);
                signal.countDown();
            }
        });

        signal.await();
        assertEquals(10, counter);

    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private float load(float amount){
        return amount;
    }

    private void loadAsync(final Task.WithProgressReporting<Float> task){
        doAsync(task, new Delegate<Float>() {
            @Override public Float invoke() {
                final int TOTAL = 10;
                for(int i = 0; i < TOTAL; i++){
                    float amount = load(i);
                    task.onProgress(amount, TOTAL);
                }
                return (float) TOTAL;
            }
        });
    }
}
