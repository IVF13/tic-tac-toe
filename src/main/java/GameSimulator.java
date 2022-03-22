
class GameSimulator {

    public static void toSimulateGame()
            throws InterruptedException {
        Gameboard gameboard = new Gameboard();
        Parser parserXML = new ParserXML();
        parserXML.toReadFile();

        Thread.sleep(1000);
        System.out.print("Введите имя 1 игрока: ");
        Thread.sleep(2000);
        System.out.println(ParserXML.getPlayers().get(0).getName());
        System.out.println();
        Thread.sleep(1000);
        System.out.print("Введите имя 2 игрока: ");
        Thread.sleep(2000);
        System.out.println(ParserXML.getPlayers().get(1).getName());
        System.out.println();
        gameboard.toPrintField();
        Thread.sleep(500);

        for (Step step : ParserXML.getStepsToRead()) {
            System.out.println("Ход игрока " + ParserXML.getPlayers().get(step.getPlayerId() - 1).getName());
            System.out.println("Выберите ячейку(1-9): ");
            Thread.sleep(1000);
            System.out.println(step.getCell());
            gameboard.setCellForSimulating(step.getCell(), ParserXML.getPlayers().get(step.getPlayerId() - 1).getSymbol());
            Thread.sleep(500);
            gameboard.toPrintField();
        }

        if (ParserXML.getGameResult().equals("Draw!")) {
            System.out.println("Ничья");
        } else {
            System.out.println(ParserXML.getPlayers().get(2).getName() + " победил");
        }

    }
}
