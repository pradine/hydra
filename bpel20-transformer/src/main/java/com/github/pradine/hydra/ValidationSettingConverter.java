package com.github.pradine.hydra;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import com.github.pradine.hydra.CommandLineArgs.ValidationSetting;

public class ValidationSettingConverter implements IStringConverter<ValidationSetting> {

    @Override
    public ValidationSetting convert(String arg0) {
        ValidationSetting setting = null;
        try {
            if (arg0 != null)
                setting = Enum.valueOf(ValidationSetting.class, arg0.toUpperCase());
        } catch (Exception e) {
            throw new ParameterException(e);
        }
        return setting;
    }
}
