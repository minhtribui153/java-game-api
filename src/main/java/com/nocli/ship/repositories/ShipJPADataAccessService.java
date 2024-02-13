package com.nocli.ship.repositories;

import com.nocli.ship.Ship;
import com.nocli.ship.interfaces.ShipDao;
import com.nocli.ship.interfaces.ShipRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class ShipJPADataAccessService implements ShipDao {
    private ShipRepository shipRepository;

    public ShipJPADataAccessService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public List<Ship> selectAllShips() {
        return shipRepository.findAll();
    }

    @Override
    public Optional<Ship> selectShipByShipId(String shipId) {
        return selectAllShips().stream()
                .filter(ship -> ship.getShipId().equals(shipId))
                .findFirst();
    }

    @Override
    public Optional<Ship> selectShipById(Integer id) {
        return shipRepository.findById(id);
    }

    @Override
    public void addShip(Ship ship) {
        shipRepository.save(ship);
    }

    @Override
    public void removeShipByShipId(String shipId) {
        selectShipByShipId(shipId).ifPresent(value -> shipRepository.delete(value));
    }

    @Override
    public void removeShipById(Integer id) {
        selectShipById(id).ifPresent(value -> shipRepository.delete(value));
    }

    @Override
    public boolean existsShipByShipId(String shipId) {
        return shipRepository.existsShipByShipId(shipId);
    }
}
