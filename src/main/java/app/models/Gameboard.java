package app.models;

import app.utils.GameConstants;

import java.util.List;
import java.util.Scanner;

public class Gameboard {
    private String[][] field = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}};

    public Gameboard() {

    }

    public Gameboard(String[][] field) {
        this.field = field;
    }

    public String[][] getField() {
        return field;
    }

    public int setCellForConsoleGame(int step, List<Player> players) {
        Scanner in = new Scanner(System.in);
        boolean isCellRight = false;
        int x = 0;
        int y = 0;
        int cell = 0;

        while (!isCellRight) {
            System.out.print("\n" +
                    GameConstants.SELECT_CELL);

            cell = in.nextInt();

            if (cell < 1 || cell > 9) {
                System.out.println(GameConstants.INVALID_VALUE + ", valid values: 1-9");
                continue;
            }

            int[] coordinates = this.toTransformCellNum(cell);
            x = coordinates[0];
            y = coordinates[1];

            if (this.field[y][x].equals(GameConstants.X) || this.field[y][x].equals(GameConstants.O)) {
                System.out.println(GameConstants.CELL_TAKEN);
            } else {
                isCellRight = true;
            }
        }

        this.field[y][x] = step % 2 == 0 ? players.get(0).getSymbol() : players.get(1).getSymbol();

        return cell;
    }

    public String toPrintField() {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < this.getField().length; j++) {

            builder.append(" |");
            for (int k = 0; k < this.getField()[j].length; k++) {
                if (this.getField()[j][k].matches("[-+]?\\d+")) {
                    builder.append("-|");
                } else {
                    builder.append(this.getField()[j][k] + "|");
                }
            }

            builder.append("\n");
        }

        return builder.toString();
    }

    public void setCellForSimulating(int cell, String symbol) {
        int[] coordinates = this.toTransformCellNum(cell);
        int x = coordinates[0];
        int y = coordinates[1];

        this.field[y][x] = symbol;
    }

    public int[] toTransformCellNum(int cell) {
        int x;
        int y;

        if (cell >= 1 && cell <= 3) {
            y = 0;
        } else if (cell >= 4 && cell <= 6) {
            y = 1;
        } else {
            y = 2;
        }

        x = (cell - 1) % 3;

        return new int[]{x, y};
    }

}
