package com.github.pradine.hydra.persistence;

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
