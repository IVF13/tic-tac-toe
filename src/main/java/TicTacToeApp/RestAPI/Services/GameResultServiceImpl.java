package TicTacToeApp.RestAPI.Services;

import java.util.ArrayList;
import java.util.List;

public class GameResultServiceImpl implements GameResultService {
    private static final List<String> RESULTS = new ArrayList<>();
    private int finishChecker = 0;

    @Override
    public String getGameResult() {
        return RESULTS.get(RESULTS.size() - 1);
    }

    @Override
    public void add(String gameResult) {
        RESULTS.add(gameResult);
    }

    @Override
    public List<String> readAll() {
        return RESULTS;
    }

    @Override
    public void deleteAll() {
        RESULTS.clear();
    }


    @Override
    public void setFinishChecker(int finishChecker) {
        this.finishChecker = finishChecker;
    }

    @Override
    public int getFinishChecker() {
        return finishChecker;
    }
}
