package com.nocli.minesweeper;

import com.nocli.exception.InvalidRequestException;

import java.util.Arrays;

public class MinesweeperBoard {
    public static final String EMPTY = "-";
    public static final String MINE = "*";
    public static final String FLAG = "^";
    private String[][] board;
    private String[][] playerBoard;
    private int mines;

    public MinesweeperBoard(int length) throws InvalidRequestException {

        if (length < 5 || length > 20) {
            throw new InvalidRequestException("Select the length of the board from 5 to 20");
        }


        this.board = new String[length][length];
        this.playerBoard = new String[length][length];
        this.mines = Math.round((float) length / 2);
        this.reset();
    }

    public String[][] getGeneratedBoard() {
        return this.board;
    }

    public void reset() {
        for (String[] y : this.board) Arrays.fill(y, EMPTY);
        for (String[] y: this.playerBoard) Arrays.fill(y, EMPTY);
    }

    public void generateNewGame() {
        // Generate Mines around the board
        int minesLeft = this.mines;
        while (minesLeft > 0) {
            for (int y = 0; y < this.board.length; y++) {
                for (int x = 0; x < this.board[y].length; x++) {
                    boolean chosen = ((int) ((Math.random() * ((this.board.length * 2) - 1)) + 1)) == this.board.length;
                    if (minesLeft > 1 && this.board[y][x].equals(EMPTY) && chosen) {
                        this.board[y][x] = MINE;
                        minesLeft -= 1;
                    }
                }
            }
        }

        // Fill in number of mines
        for (int y = 0; y < this.board.length; y++) {
            for (int x = 0; x < this.board[y].length; x++) {
                int nearMines = this.countNearMines(x, y);
                if (nearMines > 0 && !isNumeric(board[y][x]) && board[y][x].equals(EMPTY)) {
                    board[y][x] = String.valueOf(nearMines);
                }
            }
        }
    }

    public boolean isNumeric(String string) {
        try {
            int number = Integer.parseInt(string);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean isMine(int x, int y) {
        // Do not allow coordinates out of the board's bounds
        if (x < 0 || x > this.board.length || y < 0 || y > this.board.length) return false;

        return this.board[y][x].equals(MINE);
    }

    private int countNearMines(int x, int y) {
        int mines = 0;


        if (isMine(x-1, y-1)) mines++;
        if (isMine(x, y-1)) mines++;
        if (isMine(x+1, y-1)) mines++;
        if (isMine(x+1, y)) mines++;
        if (isMine(x+1, y+1)) mines++;
        if (isMine(x, y+1)) mines++;
        if (isMine(x-1, y+1)) mines++;
        if (isMine(x-1, y)) mines++;

        return mines;
    }


}
