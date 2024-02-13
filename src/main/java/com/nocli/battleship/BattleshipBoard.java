package com.nocli.battleship;

import com.nocli.battleship.records.information.AttackStatus;
import com.nocli.battleship.records.information.ShipInformation;
import com.nocli.exception.InvalidRequestException;
import com.nocli.ship.Ship;

import java.util.*;

public class BattleshipBoard {
    public static final String WATER = "-";
    public static final String HIT = "X";
    public static final String MISS = "O";

    /*
        Game Board
        [ [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] ], 0
        [ [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] ], 1
        [ [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] ], 2
        [ [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] ], 3
        [ [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] ], 4
        [ [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] ], 5
        [ [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] ], 6
        [ [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] ], 7
        [ [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] ], 8
        [ [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] ]  9
    */
    private final String[][] board;
    private final String[][] attackBoard;
    private final int length;
    private final Ship[] ships;
    private final List<Ship> remainingShips;
    private List<Ship> usedShips;
    private Map<Integer, ShipInformation> healthMap;
    private boolean started;

    public BattleshipBoard(int length, Ship[] ships) {
        String[][] newBoard = new String[length][length];

        for (String[] strings : newBoard) {
            Arrays.fill(strings, BattleshipBoard.WATER);
        }

        String[][] newAttackBoard = new String[length][length];

        for (String[] strings : newAttackBoard) {
            Arrays.fill(strings, BattleshipBoard.WATER);
        }

        this.started = false;
        this.ships = ships;
        this.length = length;
        this.board = newBoard;
        this.attackBoard = newAttackBoard;
        this.usedShips = new ArrayList<>();
        this.healthMap = new HashMap<>();
        this.remainingShips = new ArrayList<>();

        this.remainingShips.addAll(Arrays.asList(ships));

        for (Ship ship : ships) {
            this.healthMap.put(ship.getId(), new ShipInformation(ship, false, ship.getLength()));
        }
    }

    public void start() {
        for (Ship ship : usedShips) {
            this.healthMap.put(ship.getId(), new ShipInformation(ship, false, ship.getLength()));
        }

        this.started = true;
    }

    public String[][] getBoard() {
        return board;
    }

    public Map<Integer, ShipInformation> getHealthMap() {
        return healthMap;
    }

    public String[][] getAttackBoard() {
        return attackBoard;
    }

    public List<Ship> getUsedShips() {
        return usedShips;
    }

    public AttackStatus attack(int targetX, int targetY) {
        String id = board[targetY][targetX];

        if (id.equals(WATER)) {
            attackBoard[targetY][targetX] = MISS;
            board[targetY][targetX] = MISS;
        } else if (id.equals(HIT) || id.equals(MISS)) {
            throw new InvalidRequestException("Coordinate already attacked");
        } else {
            for (Ship ship : ships) {
                if (id.equals(String.valueOf(ship.getId()))) {
                    ShipInformation shipInformation = healthMap.get(ship.getId());

                    if (shipInformation != null) {
                        int health = shipInformation.health() - 1;
                        boolean destroyed = health < 1;

                        healthMap.put(ship.getId(), new ShipInformation(ship, destroyed, health));
                        attackBoard[targetY][targetX] = HIT;
                        board[targetY][targetX] = HIT;

                        return new AttackStatus(true, ship, destroyed ? ship : null, attackBoard, board);
                    }
                }
            }
        }

        return new AttackStatus(false, null, null, attackBoard, board);
    }

    public void placeShip(Ship ship, int startX, int startY, String direction) {
        switch (direction) {
            case "up" -> {
                if ((startY - ship.getLength()) < -1) throw new InvalidRequestException("Ship out of bounds because of invalid coordinate for up direction");
                checkForIntersections(startX, startY, false, ship.getLength(), -1);
                // x must remain the same
                for (int y = 0; y < ship.getLength(); y++) setForAxis(ship, startX, startY - y);
            }
            case "down" -> {
                if ((startY + ship.getLength()) > length) throw new InvalidRequestException("Ship out of bounds because of invalid coordinate for down direction");
                checkForIntersections(startX, startY, false, ship.getLength(), 1);
                // x must remain the same
                for (int y = 0; y < ship.getLength(); y++) setForAxis(ship, startX, startY + y);
            }
            case "left" -> {
                if ((startX - ship.getLength()) < -1) throw new InvalidRequestException("Ship out of bounds because of invalid coordinate for left direction");
                checkForIntersections(startX, startY, true, ship.getLength(), -1);
                for (int x = 0; x < ship.getLength(); x++) {
                    // y must remain the same
                    setForAxis(ship, startX - x, startY);
                }
            }
            case "right" -> {
                if ((startX + ship.getLength()) > length) throw new InvalidRequestException("Ship out of bounds because of invalid coordinate for right direction");
                checkForIntersections(startX, startY, true, ship.getLength(), 1);
                for (int x = 0; x < ship.getLength(); x++) {
                    // y must remain the same
                    setForAxis(ship, startX + x, startY);
                }
            }
        }

        usedShips.add(ship);
        remainingShips.remove(ship);
    }

    public List<Ship> getRemainingShips() {
        return remainingShips;
    }

    public void deleteShip(Ship ship) {
        if (!usedShips.contains(ship)) throw new InvalidRequestException("No such ship called \"%s\" inside the board".formatted(ship.getName()));
        deleteForAxis(ship);
        usedShips.remove(ship);
        remainingShips.add(ship);
    }

    public String getForAxis(int x, int y) {
        return board[y][x];
    }

    public void setForAxis(Ship ship, int x, int y) {
        if (x < -1 || x > length || y < -1 || y > length) throw new InvalidRequestException("Invalid coordinates");
        board[y][x] = String.valueOf(ship.getId());
    }

    public void deleteForAxis(Ship ship) {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x].equals(String.valueOf(ship.getId()))) {
                    board[y][x] = WATER;
                }
            }
        }
    }

    public void checkForIntersections(int startX, int startY, boolean isCoordinateX, int shipLength, int increment) {
        for (int i = 0; i < shipLength; i++) {
            String object = isCoordinateX
                ? getForAxis(startX + i * increment, startY)
                : getForAxis(startX, startY + i * increment);
            if (!object.equals(BattleshipBoard.WATER)) {
                Optional<Ship> ship = Arrays.stream(ships).filter(s -> String.valueOf(s.getId()).equals(object)).findFirst();

                if (ship.isPresent()) {
                    throw new InvalidRequestException("Ship intersected %s".formatted(ship.get().getName()));
                } else {
                    throw new InvalidRequestException("Ship intersected %s".formatted(object));
                }

            };
        }
    }

    public boolean hasStarted() {
        return started;
    }

    @Override
    public String toString() {
        return "Board{" +
                "board=" + Arrays.toString(board) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BattleshipBoard battleshipBoard1 = (BattleshipBoard) o;
        return Arrays.equals(board, battleshipBoard1.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }
}
