package com.nocli.ship.repositories;

import com.nocli.ship.Ship;
import com.nocli.ship.interfaces.ShipDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class ShipDataAccessService implements ShipDao {
    private List<Ship> ships;

    public ShipDataAccessService() { this.ships = new ArrayList<>(); }


    @Override
    public List<Ship> selectAllShips() {
        return ships;
    }

    @Override
    public Optional<Ship> selectShipByShipId(String shipId) {
        return ships.stream()
                .filter(ship -> ship.getShipId().equals(shipId))
                .findFirst();
    }

    @Override
    public Optional<Ship> selectShipById(Integer id) {
        return ships.stream()
                .filter(ship -> ship.getId() == id)
                .findFirst();
    }

    @Override
    public void addShip(Ship ship) {
        ships.add(ship);
    }

    @Override
    public void removeShipByShipId(String shipId) {
        selectShipByShipId(shipId).ifPresent(value -> ships.remove(value));
    }

    @Override
    public void removeShipById(Integer id) {
        selectShipById(id).ifPresent(value -> ships.remove(value));
    }

    @Override
    public boolean existsShipByShipId(String shipId) {
        return ships.stream()
                .anyMatch(ship -> ship.getShipId().equals(shipId));
    }
}
