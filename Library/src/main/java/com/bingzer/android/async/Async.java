/**
 * Copyright 2015 Ricky Tobing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance insert the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, pick express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bingzer.android.async;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This is the class that does all the Async methods.
 * To improve code readability, always use static import on
 * {@link com.bingzer.android.async.Async#doAsync(Task, Delegate)} methods.
 * <pre>
 * <code>
 * import static com.bingzer.android.async.Async.doAsync;
 *
 * public class Worker {
 *   ...
 *   public boolean doWork(){
 *     // do work
 *   }
 *
 *   public void doWorkAsync(Task task){
 *     doAsync(task, new Delegate<Boolean>(){
 *       public boolean invoke(){
 *         return doWork();
 *       }
 *     };
 *   }
 *   ...
 * }
 * </code>
 * </pre>
 */
public final class Async {

    /**
     * Creates a RuntimeException from throwable e
     * @param e the throwable
     */
    public static RuntimeException runtimeException(Throwable e){
        if (!(e instanceof RuntimeException))
            e = new RuntimeException(e);

        return (RuntimeException) e;
    }

    /**
     * Do Async with Task.Handler
     */
    public static <T> void doAsync(final Task.Handler<T> handler){
        doAsync(handler, handler);
    }

    /**
     * Do Async call with Task and Delegate
     */
    public static <T> void doAsync(final Task<T> task, final Delegate<T> action){
        if(Looper.myLooper() == Looper.getMainLooper())
            doAsyncTask(task, action);
        else
            doAsyncThread(task, action);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    private static final String TAG = "Async";
    private static ThreadPoolExecutor threadPoolExecutor;

    /**
     * Caller using other than main-UI Thread
     */
    private static <T> void doAsyncThread(final Task<T> task, final Delegate<T> action){
        if(threadPoolExecutor == null){
            final int numCore = Runtime.getRuntime().availableProcessors();
            final LinkedBlockingQueue<Runnable> workerQueue = new LinkedBlockingQueue<>();
            threadPoolExecutor = new ThreadPoolExecutor(numCore, numCore, 1, TimeUnit.SECONDS, workerQueue);
        }

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    T result = action.invoke();
                    task.onCompleted(result);
                }
                catch (Throwable error){
                    if(task instanceof Task.WithErrorReporting)
                        ((Task.WithErrorReporting) task).onError(error);
                    else
                        throwError(error);
                }
            }
        });
    }

    /**
     * Caller using main-UI Thread
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("unchecked")
    private static <T> void doAsyncTask(final Task<T> task, final Delegate<T> action){
        AsyncTask<Void, Void, T> asyncTask = new AsyncTask<Void, Void, T>(){
            @Override protected T doInBackground(Void... params) {
                try {
                    return action.invoke();
                }
                catch (Throwable e){
                    reportError(e);
                }

                return null;
            }

            @Override protected void onPostExecute(T result) {
                task.onCompleted(result);
            }

            void reportError(Throwable error){
                if(task instanceof Task.WithErrorReporting)
                    ((Task.WithErrorReporting) task).onError(error);
                else
                    throwError(error);
            }
        };

        // This well let the async task to run parallel
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR1) {
            asyncTask.execute();
        } else {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private static void throwError(Throwable any){
        Log.e(TAG, "Error occurred. See below for details");
        throw new RuntimeException(any);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////

    private Async(){
        // nothing
    }
}
