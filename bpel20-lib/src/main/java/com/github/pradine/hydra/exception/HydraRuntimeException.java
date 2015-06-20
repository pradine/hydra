package com.github.pradine.hydra.exception;

/*
 * #%L
 * bpel20-lib
 * %%
 * Copyright (C) 2015 the original author or authors.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class HydraRuntimeException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -1968744262384889002L;

    public HydraRuntimeException() {
        super();
    }

    public HydraRuntimeException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HydraRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public HydraRuntimeException(String message) {
        super(message);
    }

    public HydraRuntimeException(Throwable cause) {
        super(cause);
    }

}
