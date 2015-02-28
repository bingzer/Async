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

import com.bingzer.android.async.Result;
import com.bingzer.android.async.Task;

public abstract class ResultHandler extends TaskHandlerBase<Result> {

    public ResultHandler(Task<Result> task) {
        super(task);
    }

    @Override
    public final Result invoke() {
        Result result = new Result();
        try {
            invokeOrThrow();
            result.setSuccess(true);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void onError(Throwable error) {
        Result result = new Result();
        result.setSuccess(false);
        result.setError(error);
        task.onCompleted(result);
    }
}
