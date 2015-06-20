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
