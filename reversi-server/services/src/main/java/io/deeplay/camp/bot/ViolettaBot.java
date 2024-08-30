package io.deeplay.camp.bot;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ViolettaBot extends BotStrategy {

    private final int depth;
    private final UpdatableUtilityFunction utilityFunction;
    private List<Tile> moves; // Список для хранения ходов
    private List<Integer> rounds;

    public ViolettaBot(int id, String name, int depth) {
        super(id, name);
        this.depth = depth;
        this.utilityFunction = new UpdatableUtilityFunction();
        this.moves = new ArrayList<>();
        this.rounds = new ArrayList<>();
        //initializeResultsFile();
    }

    private void initializeResultsFile() {
        try (BufferedWriter writerWins = new BufferedWriter(new FileWriter("TotalWins.txt"));
             BufferedWriter writerGames = new BufferedWriter(new FileWriter("TotalGames.txt"))) {
            for (int i = 0; i < 60; i++) {
                writerWins.write("0.0 ".repeat(64).trim());
                writerWins.newLine();
                writerGames.write("0.0 ".repeat(64).trim());
                writerGames.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        List<Tile> allTiles = boardLogic.getAllValidTiles(currentPlayerId);
        if (allTiles.isEmpty()) {
            return null;
        }

        Tile bestMove = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (Tile tile : allTiles) {
            // Клонируем доску
            BoardService clonedBoard = boardLogic.getCopy();
            clonedBoard.makeMove(currentPlayerId, tile); // Совершаем ход

            // Оценка хода с помощью метода минимакс
            double score = minimax(clonedBoard, depth - 1, false, currentPlayerId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (score > bestScore) {
                bestScore = score;
                bestMove = tile;
            }
        }
        rounds.add(boardLogic.getRound());
        moves.add(bestMove); // Добавляем ход в список
        BoardService clonedBoard = boardLogic.getCopy();
        clonedBoard.makeMove(currentPlayerId, bestMove);
        if(clonedBoard.checkForWin().isGameFinished()){
            onGameEnd(clonedBoard.checkForWin().getUserIdWinner(), currentPlayerId);
        }
        return bestMove;
    }

    public void onGameEnd(int winnerId, int currentPlayerId) {
        // Обновление файлов с результатами
        updateResultsFile(winnerId, currentPlayerId);
    }

    private void updateResultsFile(int winnerId, int currentPlayerId) {
        double[][] totalGames = new double[60][64]; // 60 ходов, 64 клетки
        double[][] totalWins = new double[60][64]; // 60 ходов, 64 клетки

        // Чтение существующих результатов
        try (BufferedReader readerGames = new BufferedReader(new FileReader("TotalGames.txt"));
             BufferedReader readerWins = new BufferedReader(new FileReader("TotalWins.txt"))) {
            for (int i = 0; i < 60; i++) {
                String lineGames = readerGames.readLine();
                String lineWins = readerWins.readLine();
                if (lineGames != null) {
                    String[] values = lineGames.split(" ");
                    for (int j = 0; j < 64; j++) {
                        totalGames[i][j] = Double.parseDouble(values[j]);
                    }
                }
                if (lineWins != null) {
                    String[] values = lineWins.split(" ");
                    for (int j = 0; j < 64; j++) {
                        totalWins[i][j] = Double.parseDouble(values[j]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Обновление массивов результатов
        for (int i = 0; i < moves.size(); i++) {
            Tile move = moves.get(i);
            int moveIndex = move.getX() * 8 + move.getY();
            totalGames[rounds.get(i)][moveIndex] = totalGames[rounds.get(i)][moveIndex] + 1; // Каждому ходу присваиваем 1 в TotalGames
            if (winnerId == currentPlayerId) {
                totalWins[rounds.get(i)][moveIndex] = totalWins[rounds.get(i)][moveIndex] + 1; // Присваиваем 1 в TotalWins, если бот выиграл
            }
        }

        // Запись обновленных результатов в файлы
        try (BufferedWriter writerGames = new BufferedWriter(new FileWriter("TotalGames.txt"));
             BufferedWriter writerWins = new BufferedWriter(new FileWriter("TotalWins.txt"));
             BufferedWriter writerResults = new BufferedWriter(new FileWriter("Results.txt"))) {
            for (int i = 0; i < 60; i++) {
                StringBuilder lineGames = new StringBuilder();
                StringBuilder lineWins = new StringBuilder();
                StringBuilder lineResults = new StringBuilder();
                for (int j = 0; j < 64; j++) {
                    lineGames.append(totalGames[i][j]).append(" ");
                    lineWins.append(totalWins[i][j]).append(" ");
                    if(totalGames[i][j] != 0){
                        lineResults.append(totalWins[i][j]/totalGames[i][j]).append(" ");
                    } else lineResults.append(0).append(" ");
                }
                writerGames.write(lineGames.toString());
                writerGames.newLine();
                writerWins.write(lineWins.toString());
                writerWins.newLine();
                writerResults.write(lineResults.toString());
                writerResults.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double minimax(BoardService board, int depth, boolean isMaximizing, int currentPlayerId, double alpha, double beta) {
        List<Tile> moves = new ArrayList<>();
        int opponentPlayerId = (currentPlayerId == 1) ? 2 : 1;
        int initialPlayerId = currentPlayerId;

        BoardService afterBoard = board.getCopy();

        // Проверяем состояние игры на каждом уровне
        if (depth == 0 || board.checkForWin().isGameFinished()) {
            return utilityFunction.evaluate(afterBoard, moves, initialPlayerId);
        }

        List<Tile> validMoves = afterBoard.getAllValidTiles(isMaximizing ? currentPlayerId : opponentPlayerId);
        if (validMoves.isEmpty()) {
            return minimax(board, depth - 1, !isMaximizing, currentPlayerId, alpha, beta); // Пропускаем ход
        }

        double bestScore = isMaximizing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        for (Tile move : validMoves) {
            afterBoard.makeMove(isMaximizing ? currentPlayerId : opponentPlayerId, move);// Совершаем ход
            moves.add(move);
            double score = minimax(afterBoard, depth - 1, !isMaximizing, currentPlayerId, alpha, beta);

            if (isMaximizing) {
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
            } else {
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
            }

            // Альфа-бета отсечение
            if (beta <= alpha) {
                break; // Выход из цикла, если отсечение произошло
            }
        }

        return bestScore;
    }

    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }
}