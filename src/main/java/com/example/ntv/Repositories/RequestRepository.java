package com.example.ntv.Repositories;

import com.example.ntv.Database.Entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository   extends JpaRepository<Request, Integer> {

}
