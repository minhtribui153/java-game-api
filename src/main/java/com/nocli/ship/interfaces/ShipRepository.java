package com.nocli.ship.interfaces;

import com.nocli.ship.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship, Integer> {
    boolean existsShipByShipId(String shipId);
    boolean existsShipById(Integer id);
}
