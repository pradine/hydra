package com.github.pradine.hydra.phase;

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

import java.io.InputStream;
import java.nio.file.Path;

import com.github.pradine.hydra.CommandLineArgs;
import com.github.pradine.hydra.CommandLineArgs.ValidationSetting;
import com.github.pradine.hydra.Phase;

/**
 * This class implements the validation phase of the application generation. The validation phase
 * consists of two sub-phases: XML schema validation followed by static-analysis, as described in
 * the WS-BPEL 2.0 specification. The main inputs to this phase are the source directory, produced
 * during the setup phase; the XML schemas for the WSDL, and WS-BPEL 2.0 files; and an XSLT file,
 * generated from Schematron schemas, to be used for static-analysis. On completion of the
 * validation phase there should be no output if the validation was successful. If the validation
 * completed unsuccessfully, however, then there will be a report containing the issues that where
 * identified.
 */
public class Validation implements Phase {
    private static final String STATIC_ANALYSIS_XSL = "com/github/pradine/hydra/xsl/static-analysis.xsl";
    
    private static final ClassLoader CLASSLOADER = Validation.class.getClassLoader();
    
    private final CommandLineArgs cla;
    
    private final Path bpelFile;
    
    private final Path sourceDir;
    
    private final ValidationSetting validation;

    public Validation(CommandLineArgs cla) {
        this.cla = cla;
        this.bpelFile = cla.getBpelFile();
        this.sourceDir = cla.getSourceDir();
        this.validation = cla.getValidation();
    }

    /*
     * (non-Javadoc) Due to the lack of a suitable XML schema-aware processor we are unable to use
     * our chosen processor to perform XML schema validation. This means that in order to perform
     * any XML schema validation at all we are forced to fall back onto the Xerces parser that
     * is shipped with Java. This means that in phase 1 we will use the JAXP API to perform the
     * XML schema validation, and in phase 2 we will use Saxon-HE for the static-analysis, as we
     * would still like to exploit the capabilities of XSLT 2.0 in this case. Hopefully, one day
     * this will no longer be an issue.
     * @see com.github.pradine.hydra.Phase#execute()
     */
    @Override
    public void execute() {
//        InputStream stream = CLASSLOADER.getResourceAsStream(STATIC_ANALYSIS_XSL);

    }
}
