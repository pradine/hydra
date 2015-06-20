package com.github.pradine.hydra;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.nio.file.NoSuchFileException;

import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.github.pradine.hydra.CommandLineArgs.ValidationSetting;

public class CommandLineArgsTest {
    private static final String FILE_NAME = "src/test/resources/com/github/pradine/hydra/file.xml";
    
    private static final String DIR_NAME = "src/test/resources/com/github/pradine/hydra/";
    
    private String[] argv;
    
    private CommandLineArgs cla;
    
    @Before
    public void setup() {
        argv = null;
        cla = null;
    }

    @Test
    public void testValidationOption() {
        //Test short name and upper-case value
        argv = new String[]{ "-va", "XSD" };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertEquals(ValidationSetting.XSD, cla.getValidation());

        //Test long name and lower-case value
        argv = new String[]{ "--validation", "sa" };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertEquals(ValidationSetting.SA, cla.getValidation());

        //Test short name and mixed-case value
        argv = new String[]{ "-va", "None" };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertEquals(ValidationSetting.NONE, cla.getValidation());

        //Test when option is omitted
        argv = new String[]{ };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertEquals(ValidationSetting.ALL, cla.getValidation());
        
        //Test unsupported value
        try {
            argv = new String[]{ "--validation", "yes" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertTrue(pe.getCause() instanceof IllegalArgumentException);
        }
        
        //Test when value is omitted
        try {
            argv = new String[]{ "-va" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertThat(pe.getMessage(), is("Expected a value after parameter -va"));
        }
    }

    @Test
    public void testBpelFileOption() {
        //Test short name
        argv = new String[]{ "-bf", FILE_NAME };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.getBpelFile().endsWith(FILE_NAME));

        //Test long name
        argv = new String[]{ "--bpel-file", FILE_NAME };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.getBpelFile().endsWith(FILE_NAME));

        //Test when option is omitted
        argv = new String[]{ };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertNull(cla.getBpelFile());
        
        //Test when file not found
        try {
            argv = new String[]{ "--bpel-file", "doesNotExist.xml" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertTrue(pe.getCause() instanceof NoSuchFileException);
        }
        
        //Test when file is omitted
        try {
            argv = new String[]{ "-bf" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertThat(pe.getMessage(), is("Expected a value after parameter -bf"));
        }
    }

    @Test
    public void testCatalogFileOption() {
        //Test short name
        argv = new String[]{ "-cf", FILE_NAME };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.getCatalogFile().endsWith(FILE_NAME));

        //Test long name
        argv = new String[]{ "--catalog-file", FILE_NAME };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.getCatalogFile().endsWith(FILE_NAME));

        //Test when option is omitted
        argv = new String[]{ };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertNull(cla.getCatalogFile());
        
        //Test when file not found
        try {
            argv = new String[]{ "--catalog-file", "doesNotExist.xml" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertTrue(pe.getCause() instanceof NoSuchFileException);
        }
        
        //Test when file is omitted
        try {
            argv = new String[]{ "-cf" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertThat(pe.getMessage(), is("Expected a value after parameter -cf"));
        }
    }

    @Test
    public void testWorkingDirectoryOption() {
        //Test short name
        argv = new String[]{ "-wd", DIR_NAME };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.getWorkingDir().endsWith(DIR_NAME));

        //Test long name
        argv = new String[]{ "--working-directory", DIR_NAME };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.getWorkingDir().endsWith(DIR_NAME));

        //Test when option is omitted
        argv = new String[]{ };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.getWorkingDir().endsWith(System.getProperty("java.io.tmpdir")));
        
        //Test when directory not found
        try {
            argv = new String[]{ "--working-directory", "src/doesNotExist" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertTrue(pe.getCause() instanceof NoSuchFileException);
        }
        
        //Test when directory is omitted
        try {
            argv = new String[]{ "-wd" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertThat(pe.getMessage(), is("Expected a value after parameter -wd"));
        }
    }

    @Test
    public void testInputDirectoryOption() {
        //Test short name
        argv = new String[]{ "-id", DIR_NAME };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.getInputDir().endsWith(DIR_NAME));

        //Test long name
        argv = new String[]{ "--input-directory", DIR_NAME };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.getInputDir().endsWith(DIR_NAME));

        //Test when option is omitted
        argv = new String[]{ };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertNull(cla.getInputDir());
        
        //Test when directory not found
        try {
            argv = new String[]{ "--input-directory", "src/doesNotExist" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertTrue(pe.getCause() instanceof NoSuchFileException);
        }
        
        //Test when directory is omitted
        try {
            argv = new String[]{ "-id" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertThat(pe.getMessage(), is("Expected a value after parameter -id"));
        }
    }

    @Test
    public void testOutputDirectoryOption() {
        //Test short name
        argv = new String[]{ "-od", DIR_NAME };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.getOutputDir().endsWith(DIR_NAME));

        //Test long name
        argv = new String[]{ "--output-directory", DIR_NAME };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.getOutputDir().endsWith(DIR_NAME));

        //Test when option is omitted
        argv = new String[]{ };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertNotNull(cla.getOutputDir());
        
        //Test when directory not found
        try {
            argv = new String[]{ "--output-directory", "src/doesNotExist" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertTrue(pe.getCause() instanceof NoSuchFileException);
        }
        
        //Test when directory is omitted
        try {
            argv = new String[]{ "-od" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertThat(pe.getMessage(), is("Expected a value after parameter -od"));
        }
    }

    @Test
    public void testVersionOption() {
        //Test short name
        argv = new String[]{ "-v" };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.isVersion());

        //Test long name
        argv = new String[]{ "--version" };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.isVersion());

        //Test when option is omitted
        argv = new String[]{ };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertFalse(cla.isVersion());

        //Test with superfluous value
        try {
            argv = new String[]{"-v", "true" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertThat(pe.getMessage(), is("Was passed main parameter 'true' but no main parameter was defined"));
        }
   }

    @Test
    public void testHelpOption() {
        //Test short name
        argv = new String[]{ "-h" };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.isHelp());

        //Test long name
        argv = new String[]{ "--help" };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertTrue(cla.isHelp());

        //Test when option is omitted
        argv = new String[]{ };
        cla = new CommandLineArgs();
        new JCommander(cla, argv);
        
        assertFalse(cla.isHelp());

        //Test with superfluous value
        try {
            argv = new String[]{"-h", "true" };
            cla = new CommandLineArgs();
            new JCommander(cla, argv);
            
            fail("Expected a ParameterException to be thrown.");
        } catch(ParameterException pe) {
            assertThat(pe.getMessage(), is("Was passed main parameter 'true' but no main parameter was defined"));
        }
    }
}
