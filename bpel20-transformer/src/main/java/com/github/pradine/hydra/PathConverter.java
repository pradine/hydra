package com.github.pradine.hydra;

/*
 * #%L
 * bpel20-transformer
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

import java.nio.file.Path;
import java.nio.file.Paths;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

public class PathConverter implements IStringConverter<Path> {

    @Override
    public Path convert(String arg0) {
        Path realPath = null;
        try {
            if (arg0 != null) {
                Path path = Paths.get(arg0);
                realPath = path.toRealPath();
            }
        } catch (Exception e) {
            throw new ParameterException(e);
        }
        return realPath;
    }
}
