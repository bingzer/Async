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

abstract class TaskHandlerBase<T> implements Task.Handler<T> {

    protected final Task<T> task;

    protected TaskHandlerBase(Task<T> task){
        if (task == null) throw new NullPointerException("task");
        this.task = task;
    }

    @Override
    public void onCompleted(T result) {
        task.onCompleted(result);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    public abstract T invokeOrThrow() throws Throwable;
    public abstract T invoke();

}
