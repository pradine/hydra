package com.github.pradine.hydra.phase;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.pradine.hydra.CommandLineArgs;
import com.github.pradine.hydra.Phase;

/**
 * The purpose of this class is to create and populate the source directory, used to store
 * the input files to the transformation process including a BPEL file; WSDLs and XSDs used
 * within the BPEL process description; and an XML catalog file used to locate the WSDL and
 * XSD files.
 */
public class Setup implements Phase {
    private static final String PREFIX = "hydra-";
    
    private static final String SOURCE_DIR = "src/main/resources/com/github/pradine/hydra/source";
    
    private static final String SETUP_XML = "com/github/pradine/hydra/xsl/setup.xsl";
    
    private final CommandLineArgs cla;
    
    private final Path bpelFile;
    
    private final Path catalogFile;
    
    private final Path inputDir;
    
    private final Path workingDir;
    
    private Path projectDir;
    
    private Path sourceDir;
    
    public Setup(CommandLineArgs cla) {
        this.cla = cla;
        this.bpelFile = cla.getBpelFile();
        this.catalogFile = cla.getCatalogFile();
        this.inputDir = cla.getInputDir();
        this.workingDir = cla.getWorkingDir();
    }
    
    @Override
    public void execute() {
        //TODO if (bpelFile || catalogFile) && inputDir are all specified then throw an error.
        
        //TODO if bpelFile is null and inputDir is null then throw an error.
        
        try {
            projectDir = Files.createTempDirectory(workingDir, PREFIX);
            projectDir.toFile().deleteOnExit();
            
            //TODO Check that trailing separators, if present, do not cause problems
            String temp = projectDir.toString();
            Path path = Paths.get(temp, SOURCE_DIR);
            sourceDir = Files.createDirectories(path);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Throw a suitable exception
        }

        cla.setProjectDir(projectDir);
        cla.setSourceDir(sourceDir);

        //TODO Copy the BPEL file to the source directory after retrieving any imported files
        
    }
}
