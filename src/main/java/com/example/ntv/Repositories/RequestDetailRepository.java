package com.example.ntv.Repositories;

import com.example.ntv.Database.Entities.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestDetailRepository  extends JpaRepository<RequestDetail, Integer> {

}
