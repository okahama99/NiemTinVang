package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.EntityWrapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityWrapperRepository extends CrudRepository<EntityWrapper, Integer> {

    EntityWrapper findByProjectId(int projectID);

    EntityWrapper findByReportId(int reportID);

    EntityWrapper findByRequestId(int requestID);

}
