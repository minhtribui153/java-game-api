package com.nocli.battleship.records.response;

import com.nocli.ship.Ship;

import java.util.List;

public record BoardResponse(
        String[][] player1Board,
        String[][] player2Board,
        List<Ship> player1RemainingShips,
        List<Ship> player2RemainingShips
) {}
