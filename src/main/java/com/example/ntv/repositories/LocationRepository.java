package com.example.ntv.repositories;

import com.example.ntv.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository  extends JpaRepository<Location, Integer> {
    Location findByStreet(String street);
}
