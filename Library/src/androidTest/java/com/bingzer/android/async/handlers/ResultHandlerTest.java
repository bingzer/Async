package com.bingzer.android.async.handlers;

import android.test.AndroidTestCase;

import com.bingzer.android.async.Result;
import com.bingzer.android.async.Task;

import java.util.concurrent.CountDownLatch;

import static com.bingzer.android.async.Async.doAsync;

public class ResultHandlerTest extends AndroidTestCase {

    int counter = 0;

    @Override
    protected void setUp() throws Exception {
        counter = 0;
    }

    public void testAsync() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        
        getResultAsync(new Task<Result>() {
            @Override
            public void onCompleted(Result result) {
                ++counter;
                signal.countDown();

                assertEquals(true, result.isSuccess());
                assertNull(result.getError());
            }
        });

        signal.await();

        assertEquals(1, counter);
    }

    public void testAsync_TryToThrow() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        // although we are throwing exception with throwResult()
        // we still get the Result object onCompleted,
        // because it's a Result
        throwResultAsync(new Task<Result>() {
            @Override public void onCompleted(Result result) {
                ++counter;
                signal.countDown();

                assertEquals(false, result.isSuccess());
                assertNotNull(result.getError());
            }
        });

        signal.await();

        assertEquals(1, counter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    private void getResultAsync(Task<Result> result){
        doAsync(new ResultHandler(result) {
            @Override public Result invokeOrThrow() throws Throwable {
                return getResult();
            }
        });
    }

    private Result getResult(){
        return new Result();
    }

    private void throwResultAsync(Task<Result> task){
        doAsync(new ResultHandler(task) {
            @Override
            public Result invokeOrThrow() throws Throwable {
                return throwResult();
            }
        });
    }

    private Result throwResult(){
        throw new RuntimeException();
    }
}
