package com.nocli.battleship.records.response;

import com.nocli.battleship.records.information.DefaultInformation;
import com.nocli.ship.Ship;

public record ShootResponse(
        boolean hit,
        Ship hitShip,
        Ship deadShip,
        DefaultInformation information
) {
}
