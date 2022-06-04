package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ReportDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDetailRepository extends CrudRepository<ReportDetail, Integer> {

}
