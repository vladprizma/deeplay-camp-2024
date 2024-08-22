package io.deeplay.camp.bot;

import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.entity.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MonteCarloEvaluatorStrategy implements EvaluationStrategy {
    private final int simulations;
    private final ExecutorService executorService;

    public MonteCarloEvaluatorStrategy(int simulations) {
        this.simulations = simulations;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public double evaluate(BoardService boardService, int currentPlayerId) {
        int wins = 0;
        int opponentId = (currentPlayerId == 1) ? 2 : 1;

        List<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < simulations; i++) {
            tasks.add(() -> {
                BoardService simulationBoard = boardService.getBoardServiceCopy();
                int currentPlayer = currentPlayerId;

                while (!simulationBoard.checkForWin().isGameFinished()) {
                    List<Tile> validMoves = simulationBoard.getAllValidTiles(currentPlayer);
                    if (!validMoves.isEmpty()) {
                        Tile randomMove = validMoves.get((int) (Math.random() * validMoves.size()));
                        simulationBoard.makeMove(currentPlayer, randomMove);
                    }
                    currentPlayer = (currentPlayer == 1) ? 2 : 1;
                }

                return simulationBoard.checkForWin().getUserIdWinner() == currentPlayerId ? 1 : 0;
            });
        }

        try {
            List<Future<Integer>> results = executorService.invokeAll(tasks);
            for (Future<Integer> result : results) {
                wins += result.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return (double) wins / simulations;
    }
}