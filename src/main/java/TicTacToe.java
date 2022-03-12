import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TicTacToe {

    public static void toPlayTicTacToe() throws IOException {
        Scanner in = new Scanner(System.in);
        boolean isFinished = false;
        int turnsCount = 0;

        String[][] field = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}};

        String[] playersNames = toIntroduce(in);

        while (!isFinished) {

            toPrintField(field);

            System.out.println("Ход игрока " + playersNames[turnsCount % 2]);

            field = toEnterValue(field, turnsCount, in);

            turnsCount++;
            isFinished = toCheckWin(field, playersNames, turnsCount);

        }
    }

    public static String[] toIntroduce(Scanner in) {
        String[] playersNames = new String[2];

        System.out.print("Введите имя 1 игрока: ");
        playersNames[0] = in.next();
        System.out.println("");
        System.out.print("Введите имя 2 игрока: ");
        playersNames[1] = in.next();
        System.out.println("");

        return playersNames;
    }

    public static void toPrintField(String[][] field) {
        System.out.println("   1: 2: 3: ");

        for (int j = 0; j < field.length; j++) {

            System.out.print(j + 1 + ": |");
            for (int k = 0; k < field[j].length; k++) {
                if (field[j][k].matches("[-+]?\\d+")) {
                    System.out.print("-|");
                } else {
                    System.out.print(field[j][k] + "|");
                }
            }

            System.out.println("");
        }
    }

    public static String[][] toEnterValue(String[][] field, int turnsCount, Scanner in) {
        boolean isCellRight = false;
        int x = 0;
        int y = 0;

        while (!isCellRight) {
            System.out.println("Выберите ячейку, введите координату(число 1-3) по оси y, а затем по оси x: ");

            x = in.nextInt() - 1;
            y = in.nextInt() - 1;

            if (x > 2 || x < 0 || y > 2 || y < 0) {
                System.out.println("Ошибка, введено неверное значение, размер поля: 3x3");
                continue;
            }

            if (field[x][y].equals("x") || field[x][y].equals("o")) {
                System.out.println("Ошибка, клетка уже занята");
            } else {
                isCellRight = true;
            }

        }

        if (turnsCount % 2 == 0) {
            field[x][y] = "x";
        } else if (turnsCount % 2 == 1) {
            field[x][y] = "o";
        }

        return field;
    }

    public static boolean toCheckWin(String[][] field, String[] playersNames, int turnsCount)
            throws IOException {

        for (int j = 0; j < field.length; j++) {
            if (field[j][0].equals(field[j][1]) && field[j][0].equals(field[j][2]) ||
                    field[0][j].equals(field[1][j]) && field[0][j].equals(field[2][j]) ||
                    field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) ||
                    field[2][0].equals(field[1][1]) && field[2][0].equals(field[0][2])) {

                if (turnsCount % 2 == 1) {
                    System.out.println(playersNames[0] + " победил");
                    toWriteScores(playersNames[0]);
                    toPrintField(field);
                    toRestartTheGame();
                    return true;
                } else if (turnsCount % 2 == 0) {
                    System.out.println(playersNames[1] + " победил");
                    toWriteScores(playersNames[1]);
                    toPrintField(field);
                    toRestartTheGame();
                    return true;
                }

            }
        }

        if (turnsCount == 9) {
            toPrintField(field);
            System.out.println("Ничья");
            toWriteScores();
            toRestartTheGame();
            return true;
        }

        return false;
    }

    public static void toWriteScores(String playerName) throws IOException {
        File file = new File("scores.txt");
        FileWriter writer = new FileWriter(file, true);
        writer.write(playerName + " победил\n");
        writer.flush();
        writer.close();
    }

    public static void toWriteScores() throws IOException {
        File file = new File("scores.txt");
        FileWriter writer = new FileWriter(file, true);
        writer.write("Ничья\n");
        writer.flush();
        writer.close();
    }

    public static void toRestartTheGame() throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Вы хотите сыграть заново? (+/-)");
        if (in.next().equals("+")) {
            toPlayTicTacToe();
        } else {
            System.exit(0);
        }

    }

}
