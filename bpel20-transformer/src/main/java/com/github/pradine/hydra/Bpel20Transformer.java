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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.github.pradine.hydra.phase.Packaging;
import com.github.pradine.hydra.phase.Setup;
import com.github.pradine.hydra.phase.Transformation;
import com.github.pradine.hydra.phase.Validation;

/**
 * This application takes a BPEL 2.0 process description and transforms it into
 * a bespoke Java process engine that implements the semantic behaviour of the
 * original BPEL 2.0 process description.
 */
public class Bpel20Transformer {
    private static final String NAME = "Hydra BPEL 2.0 Transformer";
    
    //TODO Get the version from the POM?
    private static final String VERSION = "0.0.1-SNAPSHOT";
    
    private static JCommander commander;

    public static void main(String[] args) {
        try {
            CommandLineArgs cla = new CommandLineArgs();
            commander = new JCommander(cla, args);
            commander.setProgramName(NAME);
            
            if (cla.isHelp()) {
                commander.usage();
            }
            else if (cla.isVersion()) {
                System.out.println("Version: " + VERSION);
            }
            else {
                Phase[] phases = {
                        new Setup(cla),
                        new Validation(cla),
                        new Transformation(cla),
                        new Packaging(cla)
                };

                for (Phase phase : phases) {
                    phase.execute();
                }
            }
        } catch (ParameterException pe) {
            commander.usage();
            pe.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        System.exit(0);
    }
}
