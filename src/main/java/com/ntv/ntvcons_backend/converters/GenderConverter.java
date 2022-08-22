package com.ntv.ntvcons_backend.converters;

import com.ntv.ntvcons_backend.constants.Gender;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/* Convert String <=> enum for Gender */
@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, String> {
    @Override
    public String convertToDatabaseColumn(Gender gender) {
        return gender.getStringValue();
    }

    @Override
    public Gender convertToEntityAttribute(String dbData) {
        return Gender.fromStringValue(dbData);
    }
}