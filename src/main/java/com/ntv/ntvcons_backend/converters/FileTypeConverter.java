package com.ntv.ntvcons_backend.converters;

import com.ntv.ntvcons_backend.constants.FileType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/* Convert String <=> enum for FileType */
@Converter(autoApply = true)
public class FileTypeConverter implements AttributeConverter<FileType, String> {
    @Override
    public String convertToDatabaseColumn(FileType fileType) {
        return fileType.getStringValue();
    }

    @Override
    public FileType convertToEntityAttribute(String dbData) {
        return FileType.fromStringValue(dbData);
    }
}