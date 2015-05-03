package com.github.pradine.hydra;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

public class PathConverter implements IStringConverter<Path> {

    @Override
    public Path convert(String arg0) {
        Path realPath = null;
        try {
            if (arg0 != null) {
                Path path = Paths.get(arg0);
                realPath = path.toRealPath();
            }
        } catch (Exception e) {
            throw new ParameterException(e);
        }
        return realPath;
    }
}
