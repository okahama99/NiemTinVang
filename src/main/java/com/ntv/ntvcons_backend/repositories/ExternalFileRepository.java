package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ExternalFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalFileRepository extends CrudRepository<ExternalFile, Integer> {
    ExternalFile findByFileName(String name);

}
