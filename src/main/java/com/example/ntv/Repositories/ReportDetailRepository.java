package com.example.ntv.Repositories;

import com.example.ntv.Database.Entities.ReportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDetailRepository   extends JpaRepository<ReportDetail, Integer> {

}
