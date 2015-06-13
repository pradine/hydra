package com.github.pradine.hydra.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.xml.namespace.QName;

import com.github.pradine.hydra.exception.HydraRuntimeException;

/**
 * This class implements a JPA 2.1 converter. It converts between <code>
 * java.xml.namespace.QName</code> objects and <code>String</code> objects.
 * 
 */
@Converter(autoApply = true)
public class QNameToStringConverter implements AttributeConverter<QName, String> {

    @Override
    public String convertToDatabaseColumn(QName qname) {
        String string = null;
        
        if (qname != null) 
            string = qname.toString();
        
        return string;
    }

    @Override
    public QName convertToEntityAttribute(String string) {
        QName qname = null;
        
        try {
            if (string != null)
                qname = QName.valueOf(string);
        }
        catch (Exception e) {
            throw new HydraRuntimeException(e);
        }
        
        return qname;
    }

}
