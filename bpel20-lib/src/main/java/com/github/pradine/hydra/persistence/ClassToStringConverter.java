package com.github.pradine.hydra.persistence;

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

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.pradine.hydra.exception.HydraRuntimeException;

/**
 * This class implements a JPA 2.1 converter. It converts between <code>Class</code>
 * objects and <code>String</code> objects.
 * 
 */
@Converter(autoApply = true)
public class ClassToStringConverter
implements AttributeConverter<Class<?>, String> {

    @Override
    public String convertToDatabaseColumn(Class<?> clazz) {
        String string = null;
        
        if (clazz != null)
            string = clazz.getName();
            
        return string;
    }

    @Override
    public Class<?> convertToEntityAttribute(String name) {
        Class<?> clazz = null;
        
        try {
            //TODO Think about OSGi environments
            if (name != null)
                clazz = Class.forName(name);
        }
        catch (Exception e) {
            throw new HydraRuntimeException(e);
        }
        
        return clazz;
    }
    
}
