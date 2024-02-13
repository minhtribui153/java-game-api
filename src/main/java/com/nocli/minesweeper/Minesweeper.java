package com.nocli.minesweeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Minesweeper {
    public static void main(String[] args) {
        MinesweeperBoard board = new MinesweeperBoard(10);
        board.generateNewGame();

        List<String> rowTextList = new ArrayList<>();

        for (String[] col : board.getGeneratedBoard()) {
            String rowText = String.join(" | ", col);
            rowTextList.add(rowText);
        }

        String separator = "";
        for (int i = 0; i < rowTextList.size(); i++) separator += "-";

        String boardText = String.join("\n" + separator + "\n", rowTextList);

        System.out.println(boardText);

    }
}
