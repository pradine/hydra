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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.annotation.Resource;
import javax.persistence.AttributeConverter;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.Serializer.Property;
import net.sf.saxon.s9api.XdmNode;

import org.springframework.beans.factory.annotation.Configurable;

import com.github.pradine.hydra.exception.HydraRuntimeException;

/**
 * The purpose of this class is to implement the functionality of a JPA 2.1 converter.
 * The reason why this converter is implemented as a proxy and a delegate, and not
 * just as a single converter class, is due to an issue that arises when using Spring
 * to configure external JPA objects. Essentially, the JPA framework initialises the
 * converter long before Spring is ready to configure it via dependency injection, and
 * so the dependency injection never occurs. The solution to this problem is to split
 * the converter into a proxy and a delegate. This allows the JPA framework to continue
 * to instantiate the converter as it needs to, while allowing the instantiation of the
 * delegate to be delayed until the converter is first used. The ultimate effect is to
 * give Spring sufficient time to get to a state whereby it can successfully configure
 * the delegate.
 * 
 * @see XdmNodeToBytesConverter
 */
@Configurable
public class XdmNodeToBytesConverterDelegate implements AttributeConverter<XdmNode, byte[]> {
    @Resource
    private Processor xmlProcessor;

    @Override
    public byte[] convertToDatabaseColumn(XdmNode node) {
        byte[] bytes = null;

        try {
            if (node != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Serializer serializer = xmlProcessor.newSerializer(baos);
                serializer.setOutputProperty(Property.INDENT, "yes");
                serializer.setOutputProperty(Property.OMIT_XML_DECLARATION, "yes");
                serializer.serializeNode(node);
                bytes = baos.toByteArray();
            }
        }
        catch (Exception e) {
            throw new HydraRuntimeException(e);
        }

        return bytes;
    }

    //TODO Decide on tiny tree or linked tree?
    @Override
    public XdmNode convertToEntityAttribute(byte[] bytes) {
        XdmNode node = null;

        try {
            if (bytes != null) {
                DocumentBuilder builder = xmlProcessor.newDocumentBuilder();
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                Source source = new StreamSource(bais);
                node = builder.build(source);
            }
        }
        catch (Exception e) {
            throw new HydraRuntimeException(e);
        }

        return node;
    }
}