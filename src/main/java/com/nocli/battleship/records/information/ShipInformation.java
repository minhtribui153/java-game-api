package com.nocli.battleship.records.information;

import com.nocli.ship.Ship;

public record ShipInformation(
        Ship ship,
        boolean destroyed,
        int health
) {}
