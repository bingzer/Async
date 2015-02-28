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
package com.bingzer.android.async.handlers;

import com.bingzer.android.async.Task;

/**
 * Ricky Tobing (2015)
 */
@SuppressWarnings("unchecked")
public abstract class VoidHandler implements Task.Handler<Void> {

    private final Task task;

    public VoidHandler(Task task) {
        if (task == null) throw new NullPointerException("task");
        this.task = task;
    }

    @Override public final Void invoke() {
        try {
            invokeOrThrow();
            return null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final void onError(Throwable error) {
        if(task instanceof Task.WithErrorReporting)
            ((Task.WithErrorReporting) task).onError(error);
        else
            throw new RuntimeException(error);
    }

    @Override
    public void onCompleted(Void result) {
        task.onCompleted(result);
    }

    /**
     * Execute
     * @throws Throwable
     */
    public abstract void invokeOrThrow() throws Throwable;
}
