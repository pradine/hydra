package com.github.pradine.hydra.persistence;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.pradine.hydra.exception.HydraRuntimeException;
import com.github.pradine.hydra.persistence.entity.Correlation2.Correlation2PK;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ConverterTest {
    @Test
    public void testClassToStringConverter() {
        String correlation2pk =
                "com.github.pradine.hydra.persistence.entity.Correlation2$Correlation2PK";
        AttributeConverter<Class<?>, String> converter = new ClassToStringConverter();
        String string = converter.convertToDatabaseColumn(Correlation2PK.class);
        
        assertThat(string, is(correlation2pk));
        
        Class<?> clazz = converter.convertToEntityAttribute(string);
        
        assertEquals(Correlation2PK.class, clazz);
        
        clazz = converter.convertToEntityAttribute(null);
        
        assertNull(clazz);
        
        string = converter.convertToDatabaseColumn(null);
        
        assertNull(string);

        try {
            clazz = converter.convertToEntityAttribute("does.not.Exist");
            
            fail("Expected a HydraRuntimeException to be thrown.");
        }
        catch (HydraRuntimeException hre) {
            assertTrue(hre.getCause() instanceof ClassNotFoundException);
        }
    }
    
    @Test
    public void testQNameTOStringConverter() {
        QName expectedQName = new QName("http://server.github.com/test", "local", "prefix");
        String expectedString = "{http://server.github.com/test}local";
        AttributeConverter<QName, String> converter = new QNameToStringConverter();
        String string = converter.convertToDatabaseColumn(expectedQName);
        
        assertThat(string, is(expectedString));
        
        QName qname = converter.convertToEntityAttribute(string);
        
        assertEquals(expectedQName, qname);
        assertThat(qname.getPrefix(), is(XMLConstants.DEFAULT_NS_PREFIX));
        
        qname = converter.convertToEntityAttribute(null);
        
        assertNull(qname);
        
        string = converter.convertToDatabaseColumn(null);
        
        assertNull(string);
        
        try {
            qname = converter.convertToEntityAttribute("{invalid");
            
            fail("Expected a HydraRuntimeException to be thrown.");
        }
        catch (HydraRuntimeException hre) {
            assertTrue(hre.getCause() instanceof IllegalArgumentException);
        }
    }
    
    @Test
    public void testUUIDTOStringConverter() {
        UUID expectedUUID = UUID.randomUUID();
        String expectedString = expectedUUID.toString();
        AttributeConverter<UUID, String> converter = new UUIDToStringConverter();
        String string = converter.convertToDatabaseColumn(expectedUUID);
        
        assertThat(string, is(expectedString));
        
        UUID uuid = converter.convertToEntityAttribute(string);
        
        assertEquals(expectedUUID, uuid);
        
        uuid = converter.convertToEntityAttribute(null);
        
        assertNull(uuid);
        
        string = converter.convertToDatabaseColumn(null);
        
        assertNull(string);
        
        try {
            uuid = converter.convertToEntityAttribute("invalid");
            
            fail("Expected a HydraRuntimeException to be thrown.");
        }
        catch (HydraRuntimeException hre) {
            assertTrue(hre.getCause() instanceof IllegalArgumentException);
        }
    }
    
    @Test
    public void testXdmNodeToBytesConverter() throws Exception {
        byte[] expectedBytes = "<data />".getBytes();
        AttributeConverter<XdmNode, byte[]> converter = new XdmNodeToBytesConverter();
        XdmNode node = converter.convertToEntityAttribute(expectedBytes);
        
        assertNotNull(node);
        
        byte[] bytes = converter.convertToDatabaseColumn(node);
        
        assertNotNull(bytes);
        assertXMLEqual("<data />", new String(bytes));
        
        node = converter.convertToEntityAttribute(null);
        
        assertNull(node);
        
        bytes = converter.convertToDatabaseColumn(null);
        
        assertNull(bytes);
        
        try {
            node = converter.convertToEntityAttribute("<invalid>".getBytes());
            
            fail("Expected a HydraRuntimeException to be thrown.");
        }
        catch (HydraRuntimeException hre) {
            assertTrue(hre.getCause() instanceof SaxonApiException);
        }
    }
}
