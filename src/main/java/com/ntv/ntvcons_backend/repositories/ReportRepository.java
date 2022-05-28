package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Report;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CrudRepository<Report, Integer> {

}
