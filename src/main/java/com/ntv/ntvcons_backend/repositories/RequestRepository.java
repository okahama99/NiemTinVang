package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Request;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends CrudRepository<Request, Integer> {

}
