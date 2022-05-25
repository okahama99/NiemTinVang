package com.example.ntv.Repositories;

import com.example.ntv.Database.Entities.ExternalFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalFileRepository extends JpaRepository<ExternalFile, Integer> {
    ExternalFile findByFileName(String name);

}
