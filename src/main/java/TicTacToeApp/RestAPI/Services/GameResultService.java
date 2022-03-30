package TicTacToeApp.RestAPI.Services;


import java.util.List;

public interface GameResultService {

    String getGameResult();

    void add(String gameResult);

    List<String> readAll();

    void deleteAll();

    int getFinishChecker();

    void setFinishChecker(int finishChecker);
}
