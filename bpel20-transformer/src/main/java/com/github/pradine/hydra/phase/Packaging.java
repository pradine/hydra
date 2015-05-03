package com.github.pradine.hydra.phase;

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
