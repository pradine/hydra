package com.github.pradine.hydra.persistence;

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
