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
import com.github.pradine.hydra.Phase;

/**
 * This class implements the transformation phase where the artifacts required for the generated
 * application are created. This phase consists of two sub-phases: a pre-processing phase, where
 * the BPEL 2.0 process description is enhanced with additional information to make the transformation
 * easier; and a transformation phase, where the enhanced BPEL process description is transformed
 * into Java objects. The main inputs into this phase are the source directory produced during the setup
 * phase; an XSLT file to perform the pre-processing; an XSLT file to perform the transformation; and
 * the location of a project directory that will store the output from each of the sub-phases. The
 * source directory, mentioned previously, is also stored below the project directory. The main outputs
 * of this phase are a Spring beans.xml file describing the Java instances used by the application, these
 * instances are based on classes provided by the Hydra runtime library; and the original source directory,
 * including all of it's contents. Also included in the output may be XML files required to generate
 * new classes that are not provided by the Hydra runtime library, but only if these additional classes
 * are needed to fully support the BPEL process. Finally, there will be a persistence XML file to list
 * any JPA classes used by the application, including any newly generated classes, if any.
 * 
 * @see Setup
 */
public class Transformation implements Phase {
    private static final String PRE_PROCESSOR_XSL = "com/github/pradine/hydra/xsl/pre-processor-main.xsl";
    
    private static final String BPEL_TRANSFORMER_XSL = "com/github/pradine/hydra/xsl/bpel-transformer-main.xsl";
    
    private static final String SPRING_DIR = "src/main/resources/com/github/pradine/hydra/spring";
    
    private static final String CLASSES_DIR = "src/main/resources/com/github/pradine/hydra/asm";
    
    private static final ClassLoader CLASSLOADER = Transformation.class.getClassLoader();
    
    private final CommandLineArgs cla;
    
    private final Path bpelFile;
    
    private final Path sourceDir;
    
    private final Path projectDir;

    public Transformation(CommandLineArgs cla) {
        this.cla = cla;
        this.bpelFile = cla.getBpelFile();
        this.sourceDir = cla.getSourceDir();
        this.projectDir = cla.getProjectDir();
    }

    /*
     * (non-Javadoc) The two phases will be implemented as a micro-pipeline in XSLT,
     * which means that the output of the first phase will simply be passed straight
     * through as the input to the second phase. This should be more efficient as we
     * will only need to invoke XSLT processor once, and there will be no need to
     * write out the temporary tree, produced as output of the first phase, and then
     * read it back in to start the second phase.
     * @see com.github.pradine.hydra.Phase#execute()
     */
    @Override
    public void execute() {
//        InputStream stream = CLASSLOADER.getResourceAsStream(BPEL_TRANSFORMER_XSL);

    }
}
