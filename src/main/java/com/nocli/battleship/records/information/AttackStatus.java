package com.nocli.battleship.records.information;

import com.nocli.ship.Ship;

public record AttackStatus(
        boolean hit,
        Ship hitShip,
        Ship deadShip,
        String[][] attackBoard,
        String[][] board
) {
}
