import java.util.Scanner;

public class Gameboard {
    private String[][] field = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}};

    public Gameboard() {

    }

    public String[][] getField() {
        return field;
    }

    public int setCell(int step, Player[] players) {
        Scanner in = new Scanner(System.in);
        boolean isCellRight = false;
        int x = 0;
        int y = 0;
        int cell = 0;

        while (!isCellRight) {

            System.out.println("Выберите ячейку(1-9): ");

            cell = in.nextInt();

            if (cell < 1 || cell > 9) {
                System.out.println("Ошибка, введено неверное значение, допустимые значения: 1-9");
                continue;


            }

            if (cell >= 1 && cell <= 3) {
                y = 0;
            } else if (cell >= 4 && cell <= 6) {
                y = 1;
            } else {
                y = 2;
            }

            x = (cell - 1) % 3;

            if (this.field[y][x].equals("X") || this.field[y][x].equals("O")) {
                System.out.println("Ошибка, клетка уже занята");
            } else {
                isCellRight = true;
            }


        }

        if (step % 2 == 0) {
            this.field[y][x] = players[0].getSymbol();
        } else if (step % 2 == 1) {
            this.field[y][x] = players[1].getSymbol();
        }

        return cell;
    }

    public void toPrintField() {

        for (int j = 0; j < this.getField().length; j++) {

            System.out.print(" |");
            for (int k = 0; k < this.getField()[j].length; k++) {
                if (this.getField()[j][k].matches("[-+]?\\d+")) {
                    System.out.print("-|");
                } else {
                    System.out.print(this.getField()[j][k] + "|");
                }
            }

            System.out.println();
        }
    }

    public void setCellForSimulating(int cell, String symbol) {
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

        this.field[y][x] = symbol;

    }

}