package com.nocli.ship.controllers;

import com.nocli.ship.Ship;
import com.nocli.ship.records.ShipRequest;
import com.nocli.ship.services.ShipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ships")
public class ShipController {
    private ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping
    List<Ship> getAllShips() {
        return shipService.getAllShips();
    }

    @PostMapping
    void createShip(@RequestBody ShipRequest request) {
        shipService.addShip(request);
    }

    @GetMapping("{id}")
    Ship getShip(@PathVariable("id") Integer id) {
        return shipService.getShipById(id);
    }

    @DeleteMapping("{id}")
    void deleteShip(@PathVariable("id") Integer id) {
        shipService.deleteShipById(id);
    }

}
