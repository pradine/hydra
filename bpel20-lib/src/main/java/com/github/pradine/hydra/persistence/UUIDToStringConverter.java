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

import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.pradine.hydra.exception.HydraRuntimeException;

/**
 * This class implements a JPA 2.1 converter. It converts between <code>
 * java.util.UUID</code> objects and <code>String</code> objects.
 * 
 */
@Converter(autoApply = true)
public class UUIDToStringConverter implements AttributeConverter<UUID, String> {

    @Override
    public String convertToDatabaseColumn(UUID uuid) {
        String string = null;
        
        if (uuid != null)
            string = uuid.toString();
        
        return string;
    }

    @Override
    public UUID convertToEntityAttribute(String string) {
        UUID uuid = null;
        
        try {
            if (string != null)
                uuid = UUID.fromString(string);
        }
        catch (Exception e) {
            throw new HydraRuntimeException(e);
        }
        
        return uuid;
    }

}
