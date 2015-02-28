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

public abstract class DelegateHandler<T> extends TaskHandlerBase<T> implements Task.WithErrorReporting<T> {

    public DelegateHandler(Task<T> task){
        super(task);
    }

    @Override public final T invoke() {
        try {
            return invokeOrThrow();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(Throwable error) {
        if(task instanceof Task.WithErrorReporting) {
            ((Task.WithErrorReporting) task).onError(error);
        }
        else throw new RuntimeException(error);
    }
}
