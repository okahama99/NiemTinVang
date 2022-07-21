package com.ntv.ntvcons_backend.converters;

import com.ntv.ntvcons_backend.constants.Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/* Convert String <=> enum for Status */
@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, String> {
    @Override
    public String convertToDatabaseColumn(Status status) {
        return status.getStringValue();
    }

    @Override
    public Status convertToEntityAttribute(String dbData) {
        return Status.fromStringValue(dbData);
    }
}