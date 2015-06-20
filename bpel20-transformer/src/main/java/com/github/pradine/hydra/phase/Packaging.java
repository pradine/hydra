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

import java.nio.file.Path;

import org.apache.maven.cli.MavenCli;

import com.github.pradine.hydra.CommandLineArgs;
import com.github.pradine.hydra.Phase;

/**
 * This class implements the packaging phase. The main inputs to this phase include all of the
 * output of the transformation phase, and a Maven POM file to turn that output into a Maven
 * project. The POM file describes how to build and package the output from the transformation
 * phase into an application archive. Once the POM file is in place we then launch an embedded
 * Maven to run it and produce the final output.
 * 
 * @see Transformation
 */
public class Packaging implements Phase {
    private static final String POM_XML = "com/github/pradine/hydra/pom/pom.xml";
    
    private final CommandLineArgs cla;
    
    private final Path projectDir;

    public Packaging(CommandLineArgs cla) {
        this.cla = cla;
        this.projectDir = cla.getProjectDir();
    }

    @Override
    public void execute() {
//        MavenCli cli = new MavenCli();
//
//        cli.doMain(new String[]{"clean", "compile"}, workingDir, System.out, System.out);
    }
}
