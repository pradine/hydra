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

import java.nio.file.Path;
import java.nio.file.Paths;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class CommandLineArgs {
    /**
     * Available validation settings.
     */
    public enum ValidationSetting {
        /**
         * Perform XML schema validation only.
         */
        XSD,
        
        /**
         * Perform static-analysis only.
         */
        SA,
        
        /**
         * Perform all supported validation. Default.
         */
        ALL,
        
        /**
         * Do not perform any validation. This can be useful if you have already validated the
         * BPEL process description using a different tool, or if you simply want to regenerate
         * the application without making any changes to the BPEL process description, having
         * validated it initially.
         */
        NONE
    };
    
    @Parameter(names = {"-va", "--validation"},
            description = "Validation settings. Not case-sensitive. Supported values include: XSD, SA, NONE, ALL (default).",
            converter = ValidationSettingConverter.class)
    private ValidationSetting validation = ValidationSetting.ALL;
    
    @Parameter(names = {"-bf", "--bpel-file"},
            description = "The file containing the WS-BPEL 2.0 process description.",
            converter = PathConverter.class)
    private Path bpelFile;
    
    @Parameter(names = {"-cf", "--catalog-file"},
            description = "An XML schema catalog file for use in resolving the locations of WSDLs and XSDs that are used by the BPEL prcoces decriptions.",
            converter = PathConverter.class)
    private Path catalogFile;
    
    @Parameter(names = {"-wd", "--working-directory"},
            description = "The directory used by the transformer to temporarily store files needed to produce the generated application. Defaults to the temp directory where the transformer is run. If specified explicitly then the directory must already exist.",
            converter = PathConverter.class)
    private Path workingDir;
    
    @Parameter(names = {"-id", "--input-directory"},
            description = "A directory containing inputs, including a BPEL file, WSDLs, XSDs, and an XML catalog file. The options '--bpel-file' and '--catalog-file' should not be used together with this option.",
            converter = PathConverter.class)
    private Path inputDir;
    
    @Parameter(names = {"-od", "--output-directory"},
            description = "The directory to which the generated application is copied, on completion. Defaults to the directory in which the transformer was run.",
            converter = PathConverter.class)
    private Path outputDir;
    
    @Parameter(names = {"-sd", "--source-directory"},
            description = "A directory, below the project directory, that contains the complete set of inputs, including a BPEL file, WSDLs, XSDs, and an XML catalog file.",
            converter = PathConverter.class,
            hidden = true)
    private Path sourceDir;
    
    @Parameter(names = {"-pd", "--project-directory"},
            description = "A directory, below the working directory, that contains the maven project setup to build and package the generated application.",
            converter = PathConverter.class,
            hidden = true)
    private Path projectDir;
    
    @Parameter(names = {"-v", "--version"},
            description = "Display the version information.")
    private boolean version;
    
    @Parameter(names = {"-h", "--help"},
            description = "Display the usage information.",
            help = true)
    private boolean help;

    public CommandLineArgs() {
        try {
            if (workingDir == null) {
                Path path = Paths.get(System.getProperty("java.io.tmpdir"));
                workingDir = path.toRealPath(); 
            }

            if (outputDir == null) {
                Path path = Paths.get(".");
                outputDir = path.toRealPath(); 
            }
        } catch (Exception e) {
            throw new ParameterException(e);
        }
    }

    public ValidationSetting getValidation() {
        return validation;
    }

    public Path getBpelFile() {
        return bpelFile;
    }

    public Path getCatalogFile() {
        return catalogFile;
    }

    public Path getWorkingDir() {
        return workingDir;
    }

    public Path getOutputDir() {
        return outputDir;
    }

    public Path getInputDir() {
        return inputDir;
    }

    public void setSourceDir(Path sourceDir) {
        this.sourceDir = sourceDir;
    }

    public Path getSourceDir() {
        return sourceDir;
    }    

    public void setProjectDir(Path projectDir) {
        this.projectDir = projectDir;
    }

    public Path getProjectDir() {
        return projectDir;
    }

    public boolean isVersion() {
        return version;
    }

    public boolean isHelp() {
        return help;
    }
}
