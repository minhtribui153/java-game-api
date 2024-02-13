package com.nocli.battleship.records.response;

public record PlayerBoardResponse(
        String[][] player1Board,
        String[][] player2Board
) {
}
