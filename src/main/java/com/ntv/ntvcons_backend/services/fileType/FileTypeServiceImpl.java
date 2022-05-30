package com.ntv.ntvcons_backend.services.fileType;

import com.ntv.ntvcons_backend.repositories.FileTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileTypeServiceImpl implements FileTypeService{
    @Autowired
    private FileTypeRepository fileTypeRepository;
}
