package com.nocli.ship.services;

import com.nocli.exception.InvalidRequestException;
import com.nocli.exception.ResourceNotFoundException;
import com.nocli.ship.Ship;
import com.nocli.ship.interfaces.ShipDao;

import com.nocli.ship.records.ShipRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipService {
    private ShipDao shipDao;

    public ShipService(@Qualifier("jpa") ShipDao shipDao) {
        this.shipDao = shipDao;
    }

    public Ship getShipByShipId(String shipId) {
        return shipDao.selectShipByShipId(shipId)
                .orElseThrow(() -> new ResourceNotFoundException("Ship with ID \"%s\" not found".formatted(shipId)));
    }

    public Ship getShipById(Integer id) {
        return shipDao.selectShipById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ship with ID \"%s\" not found".formatted(id)));
    }

    public List<Ship> getAllShips() {
        return shipDao.selectAllShips();
    }

    public void addShip(ShipRequest request) {

        Optional<Ship> existingShip = shipDao.selectShipByShipId(request.shipId());

        if (existingShip.isPresent()) throw new InvalidRequestException("Another ship with ID \"%s\" exists".formatted(request.shipId()));

        Ship newShip = new Ship(request.name(), request.length(), request.shipId());
        shipDao.addShip(newShip);
    }

    public void deleteShipByShipId(String shipId) {
        Ship ship = getShipByShipId(shipId);

        shipDao.removeShipByShipId(ship.getShipId());
    }

    public void deleteShipById(Integer id) {
        Ship ship = getShipById(id);

        shipDao.removeShipById(ship.getId());
    }
}
