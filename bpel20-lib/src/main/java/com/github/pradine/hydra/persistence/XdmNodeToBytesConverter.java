package com.github.pradine.hydra.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import net.sf.saxon.s9api.XdmNode;

/**
 * This class is a proxy that works together with a delegate to provide the functionality
 * of a JPA 2.1 converter. It converts between <code>net.sf.saxon.s9api.XdmNode</code>
 * objects and a <code>byte[]</code>.
 *
 * @see XdmNodeToBytesConverterDelegate
 */
@Converter(autoApply = true)
public class XdmNodeToBytesConverter implements AttributeConverter<XdmNode, byte[]> {
    private volatile AttributeConverter<XdmNode, byte[]> delegate;

    @Override
    public byte[] convertToDatabaseColumn(XdmNode node) {
        initialize();
        
        byte[] bytes = delegate.convertToDatabaseColumn(node);
        
        return bytes;
    }

    @Override
    public XdmNode convertToEntityAttribute(byte[] bytes) {
        initialize();
        
        XdmNode node = delegate.convertToEntityAttribute(bytes);
        
        return node;
    }

    private void initialize() {
        if (delegate == null) {
            synchronized(this) {
                if (delegate == null)
                    delegate = new XdmNodeToBytesConverterDelegate();
            }
        }
    }
}
