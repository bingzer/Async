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
 * Result is a container that carries an exception
 */
@SuppressWarnings("unused")
public class Result {
    private boolean success;
    private Throwable error;

    /**
     * Creates a result. This constructor will create
     * a good result { success = true, error = null }
     */
    public Result(){
        this(true, null);
    }

    /**
     * Creates a result with an error. This constructor will create
     * a bad result { success = false, error = any }
     * @param error any throwable type
     */
    public Result(Throwable error){
        this(false, error);
    }

    /**
     * Creates a result with specified value
     * @param success maybe true/false
     * @param error any throwable type
     */
    public Result(boolean success, Throwable error){
        this.success = success;
        this.error = error;
    }

    /**
     * True if success, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Pragmatically sets success to specified boolean value
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Returns the error if any
     */
    public Throwable getError() {
        return error;
    }

    /**
     * Pragmatically sets an the error
     */
    public void setError(Throwable error) {
        this.error = error;
    }

    /**
     * Returns the string representation of this Result object
     */
    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", error=" + error +
                '}';
    }
}
