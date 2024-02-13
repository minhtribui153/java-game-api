package com.nocli.ship.interfaces;

import com.nocli.ship.Ship;

import java.util.List;
import java.util.Optional;

public interface ShipDao {
    List<Ship> selectAllShips();
    Optional<Ship> selectShipByShipId(String shipId);
    Optional<Ship> selectShipById(Integer id);
    void addShip(Ship ship);
    void removeShipByShipId(String shipId);
    void removeShipById(Integer id);
    boolean existsShipByShipId(String shipId);
}
