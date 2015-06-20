package com.github.pradine.hydra.schematron;

/*
 * #%L
 * bpel20-transformer
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

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import org.junit.BeforeClass;
import org.junit.Test;

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
    public void testSA00006() throws Exception {
        evaluate("sa00006/valid.bpel", "sa00006/validResult.xml");
        evaluate("sa00006/invalid.bpel", "sa00006/invalidResult.xml");
    }

    private void evaluate(String testFile, String resultFile) throws Exception {
        XsltTransformer transformer = executable.load();
        InputStream testIs   = this.getClass().getResourceAsStream(testFile);        
        InputStream resultIs = this.getClass().getResourceAsStream(resultFile);        
        Reader resultReader = new InputStreamReader(resultIs);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Serializer serializer = executable.getProcessor().newSerializer(baos);
        Source source = new StreamSource(testIs);
        source.setSystemId(testFile);
        transformer.setSource(source);
        transformer.setDestination(serializer);
        transformer.transform();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Reader output = new InputStreamReader(bais);
        
        assertXMLEqual(resultReader, output);        
    }
}
