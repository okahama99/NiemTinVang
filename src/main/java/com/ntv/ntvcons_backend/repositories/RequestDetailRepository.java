package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.RequestDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestDetailRepository extends CrudRepository<RequestDetail, Integer> {

}
