package com.example;

import com.bingzer.android.async.Task;
import com.bingzer.android.async.handlers.VoidHandler;

import static com.bingzer.android.async.Async.doAsync;

/**
 * Ricky Tobing (2015)
 */
@SuppressWarnings("unused")
public class TestUsage {
    public void test(){
    }


    //////////////////////////////////////////////////////////////////////////////////////

    private void doWork(){

    }

    private void doWorkAsync(Task task){
        doAsync(new VoidHandler(task) {
            @Override
            public void invokeOrThrow() throws Throwable {
                doWork();
            }
        });
    }
}
