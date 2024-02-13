package com.nocli.battleship;

import com.nocli.battleship.records.information.AttackStatus;
import com.nocli.battleship.records.request.GuildRequest;
import com.nocli.exception.InvalidRequestException;
import com.nocli.exception.ResourceNotFoundException;
import com.nocli.ship.Ship;

import java.util.*;

public class Battleship {
    private boolean player1Ready;
    private boolean player2Ready;
    private GuildRequest guild;
    private String player1MessageId;
    private String player2MessageId;
    private final String player1Id;
    private final String player2Id;
    private final BattleshipBoard player1BattleshipBoard;
    private final BattleshipBoard player2BattleshipBoard;
    private final Ship[] ships;

    public Battleship(String player1Id, String player2Id, Ship[] ships, GuildRequest guild, String player1MessageId, String player2MessageId) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;

        this.player1BattleshipBoard = new BattleshipBoard(10, ships);
        this.player2BattleshipBoard = new BattleshipBoard(10, ships);

        this.guild = guild;
        this.player1MessageId = player1MessageId;
        this.player2MessageId = player2MessageId;
        this.player1Ready = false;
        this.player2Ready = false;
        this.ships = ships;
    }

    public Optional<Ship> getShip(String shipName) {
        return Arrays.stream(ships)
                .filter(ship -> Objects.equals(ship.getName(), shipName))
                .findFirst();
    }

    public String getPlayer1Id() {
        return player1Id;
    }

    public String getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer1MessageId(String player1MessageId) {
        this.player1MessageId = player1MessageId;
    }

    public void setPlayer2MessageId(String player2MessageId) {
        this.player2MessageId = player2MessageId;
    }

    public void setGuild(GuildRequest guild) {
        this.guild = guild;
    }

    public int getPlayerNumById(String playerId) {
        if (!playerId.equals(this.getPlayer1Id()) && !playerId.equals(this.getPlayer2Id()))
            throw new InvalidRequestException("Player with ID \"%s\" is not in this game".formatted(playerId));

        return playerId.equals(this.player1Id) ? 1 : 2;
    }

    public String getPlayerIdByNum(Integer playerNum) {
        if (playerNum != 1 && playerNum != 2)
            throw new InvalidRequestException("Invalid player number %s. Choose 1 or 2.".formatted(playerNum));

        return playerNum == 1 ? player1Id : player2Id;
    }


    public void setReady(int playerNum) {
        if (playerNum != 1 && playerNum != 2) throw new InvalidRequestException("Player number \"%s\" not found".formatted(String.valueOf(playerNum)));
        if (playerNum == 1) {
            this.player1Ready = true;
        } else {
            this.player2Ready = true;
        }

        if (this.player1Ready && this.player2Ready) {
            this.player1BattleshipBoard.start();
            this.player2BattleshipBoard.start();
        }
    }

    public boolean getReady(int playerNum) {
        return playerNum == 1 ? player1Ready : player2Ready;
    }

    public void placeShip(String shipName, int startX, int startY, String direction, int playerId) {
        Ship ship = getShip(shipName)
                .orElseThrow(() -> new ResourceNotFoundException("Ship \"%s\" not found".formatted(shipName)));

        BattleshipBoard battleshipBoard = getBoardById(playerId);

        if (!battleshipBoard.getRemainingShips().contains(ship))
            throw new InvalidRequestException("Ship \"%s\" has already been used".formatted(shipName));

        String correctDirection = getValidDirectionOrThrow(direction);

        battleshipBoard.placeShip(ship, startX, startY, correctDirection);
    }

    public AttackStatus fire(int targetX, int targetY, int playerId) {
        BattleshipBoard battleshipBoard = getBoardById(playerId);

        return battleshipBoard.attack(targetX, targetY);
    }

    public void removeShip(String shipName, int playerId) {
        Ship ship = getShip(shipName)
                .orElseThrow(() -> new ResourceNotFoundException("Ship \"%s\" not found".formatted(shipName)));

        BattleshipBoard battleshipBoard = getBoardById(playerId);

        battleshipBoard.deleteShip(ship);
    }

    public String getValidDirectionOrThrow(String direction) {
        String correctDirection = direction.toLowerCase();
        if (
                !correctDirection.equals("up") &&
                !correctDirection.equals("down") &&
                !correctDirection.equals("left") &&
                !correctDirection.equals("right")
        ) {
            throw new InvalidRequestException("Invalid direction. Choose up, down, left or right");
        }

        return correctDirection;
    }

    public String getPlayer1MessageId() {
        return player1MessageId;
    }

    public String getPlayer2MessageId() {
        return player2MessageId;
    }

    public GuildRequest getGuild() {
        return guild;
    }

    public BattleshipBoard getBoardById(int playerId) {
        if (playerId == 1) {
            return player1BattleshipBoard;
        } else if (playerId == 2) {
            return player2BattleshipBoard;
        } else throw new InvalidRequestException("Invalid player number. Choose player 1 or 2");
    }
}
