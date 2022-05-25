package com.example.ntv.Repositories;

import com.example.ntv.Database.Entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository   extends JpaRepository<Report, Integer> {

}
