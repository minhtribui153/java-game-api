package com.nocli.battleship.records.response;

import com.nocli.ship.Ship;

import java.util.List;

public record ManageShipResponse(Integer playerNumber, String playerId, String[][] playerBoard, List<Ship> usedShips, List<Ship> remainingShips) {

}
