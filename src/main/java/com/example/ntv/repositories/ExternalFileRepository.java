package com.example.ntv.repositories;

import com.example.ntv.entities.ExternalFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalFileRepository extends JpaRepository<ExternalFile, Integer> {
    ExternalFile findByFileName(String name);
}
