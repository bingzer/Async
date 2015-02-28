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

/**
 * Represents a Task that wants to be completed. Upon a completion of a {@code Task},
 * the method {@link Task#onCompleted(Object)} will be invoked, returning a {@code result} object
 * <p>
 * When using {@linkplain Task}, the default error behavior is to rethrow whenever
 * {@linkplain Delegate#invoke()} encounter an error/exception.
 * </p>
 * <p>
 * For custom error handling, use {@linkplain Task.WithErrorReporting}.
 * This interface allows a customization of error handling.
 * {@linkplain Task.WithErrorReporting#onError(Throwable)} will handle any
 * {@linkplain java.lang.Throwable} thrown inside {@linkplain Delegate#invoke()}
 * </p>
 *
 * @see com.bingzer.android.async.Delegate
 */
public interface Task<T> {

    /**
     * Called when a task is completed
     * @param result result
     */
    void onCompleted(T result);

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Extension if you need some kind of explanation
     * what a task failed to complete
     * @param <T>
     */
    public static interface WithErrorReporting<T> extends Task<T> {

        /**
         * Called when there's an error during the invocation
         */
        void onError(Throwable error);

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Extension if you need a progress reporting back.
     * The progress report is implemented by the client
     * @param <T>
     */
    public static interface WithProgressReporting<T> extends WithErrorReporting<T> {

        /**
         * Called when progress is made
         * @param current the current progress
         * @param total the total
         */
        void onProgress(float current, float total);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Handler is a composite type of {@link com.bingzer.android.async.Task} and
     * {@link com.bingzer.android.async.Delegate}. This is a convenient way to declare a Task
     * that's also a Delegate.
     * <p>
     * There are several concrete implementation of Handlers that maybe useful:
     * <ul>
     * <li>{@link com.bingzer.android.async.handlers.VoidHandler}</li>
     * <li>{@link com.bingzer.android.async.handlers.DelegateHandler}</li>
     * <li>{@link com.bingzer.android.async.handlers.ResultHandler}</li>
     * </ul>
     * </p>
     * <p>
     * Example of implementation<br/>
     * <pre>
     * <code>
     * ...
     * public void doWork(){
     *     // do work here
     * }
     *
     * public void doWorkAsync(Task task){
     *     doAsync(new VoidHandler(task){
     *         public void invokeOrThrow() throws Throwable {
     *             doWork();
     *         }
     *     }
     * }
     * ...
     * </code>
     * </pre>
     * </p>
     * @param <T>
     */
    public static interface Handler<T> extends Task.WithErrorReporting<T>, Delegate<T> {

    }
}
