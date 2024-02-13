package com.nocli.battleship.records.response;

import com.nocli.ship.Ship;

import java.util.List;

public record BattleshipResponse(
        String player1Id,
        String player2Id,
        String[][] player1Board,
        String[][] player2Board,
        String gameId,
        List<Ship> ships
) {}
