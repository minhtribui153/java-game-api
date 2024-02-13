package com.nocli.battleship.records.information;

import com.nocli.ship.Ship;

import java.util.List;

public record PlayerInformation(
        String playerId,
        String[][] board,
        String[][] attackBoard,

        List<ShipInformation> usedShips,
        List<Ship> remainingShips
) {}
