package com.github.pradine.hydra;

/**
 * Each phase provides a step in the transformation of a WS-BPEL 2.0 process description into
 * a Java application.
 */
public interface Phase {
    /**
     * Execute the phase.
     */
    public void execute();
}
