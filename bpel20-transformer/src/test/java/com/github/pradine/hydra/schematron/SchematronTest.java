package com.github.pradine.hydra.schematron;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

public class SchematronTest {
    private static final String STATIC_ANALYSIS_XSL =
            "target/generated-resources/com/github/pradine/hydra/xsl/static-analysis.xsl";

    private static XsltExecutable executable;
    
    @BeforeClass
    public static void compileXslt() throws Exception {
        Processor processor = new Processor(false);
        XsltCompiler compiler = processor.newXsltCompiler();
        Path path = Paths.get(STATIC_ANALYSIS_XSL).toRealPath(); 
        executable = compiler.compile(new StreamSource(path.toFile()));
    }

    @Test
    public void testValidSA00006() throws Exception {
        XsltTransformer transformer = executable.load();
        String testFile   = "sa00006/valid.bpel";
        String resultFile = "sa00006/validResult.xml";
        InputStream testIs   = this.getClass().getResourceAsStream(testFile);        
        InputStream resultIs = this.getClass().getResourceAsStream(resultFile);        
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document resultDoc = db.parse(resultIs, resultFile);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Serializer serializer = executable.getProcessor().newSerializer(baos);
        Source source = new StreamSource(testIs);
        source.setSystemId(testFile);
        transformer.setSource(source);
        transformer.setDestination(serializer);
        transformer.transform();
        
        db.reset();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Document output = db.parse(bais);
        assertXMLEqual(resultDoc, output);
    }

    @Test
    public void testInvalidSA00006() throws Exception {
        XsltTransformer transformer = executable.load();
        String testFile   = "sa00006/invalid.bpel";
        String resultFile = "sa00006/invalidResult.xml";
        InputStream testIs   = this.getClass().getResourceAsStream(testFile);        
        InputStream resultIs = this.getClass().getResourceAsStream(resultFile);        
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document resultDoc = db.parse(resultIs, resultFile);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Serializer serializer = executable.getProcessor().newSerializer(baos);
        Source source = new StreamSource(testIs);
        source.setSystemId(testFile);
        transformer.setSource(source);
        transformer.setDestination(serializer);
        transformer.transform();
        
        db.reset();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Document output = db.parse(bais);
        assertXMLEqual(resultDoc, output);
    }

}
