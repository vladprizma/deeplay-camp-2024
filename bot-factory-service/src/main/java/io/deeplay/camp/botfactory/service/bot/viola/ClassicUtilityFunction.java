package io.deeplay.camp.botfactory.service.bot.viola;


import io.deeplay.camp.botfactory.service.board.BoardService;
import io.deeplay.camp.botfactory.service.bot.darling.EvaluationStrategy;

import java.util.*;

public class ClassicUtilityFunction implements EvaluationStrategy {

    @Override
    public double evaluate(BoardService board, int currentPlayerId) {
        double score = 0;
        int opponentId = (currentPlayerId == 1) ? 2 : 1;
        score += evaluateCorners(board, currentPlayerId) * 5;  // Углы
        score += evaluateEdges(board, currentPlayerId);  // Края
        score += evaluateStableDiscs(board, currentPlayerId) * 2;  // Стабильные фишки
        score += board.score()[currentPlayerId - 1] - board.score()[opponentId - 1];  // Счет
        return score;
    }

    private double evaluateStableDiscs(BoardService board, int currentPlayerId) {
        double stableScore = 0;

        // Подсчитайте стабильные фишки
        stableScore += countStableDiscs(board, currentPlayerId);
        stableScore -= countStableDiscs(board, currentPlayerId == 1 ? 2 : 1);

        return stableScore;
    }

    public int countStableDiscs(BoardService board, int currentPlayerId) {
        int stableDiscs = 0;

        // Проверяем углы
        stableDiscs += checkCorner(board, 0, 0, currentPlayerId);
        stableDiscs += checkCorner(board, 0, 7, currentPlayerId);
        stableDiscs += checkCorner(board, 7, 0, currentPlayerId);
        stableDiscs += checkCorner(board, 7, 7, currentPlayerId);

        // Проверяем стабильные фишки рядом с углами
        stableDiscs += checkAdjacentToCorner(board, 0, 0, currentPlayerId);
        stableDiscs += checkAdjacentToCorner(board, 0, 7, currentPlayerId);
        stableDiscs += checkAdjacentToCorner(board, 7, 0, currentPlayerId);
        stableDiscs += checkAdjacentToCorner(board, 7, 7, currentPlayerId);

        return stableDiscs;
    }

    private int checkCorner(BoardService board, int x, int y, int playerId) {
        // Проверяем, занята ли угловая клетка текущим игроком
        if (playerId == 1) {
            if (board.hasPieceBlack(x, y)) {
                return 1; // Угловая фишка стабильна
            }
        }else if (playerId == 2) {
            if (board.hasPieceWhite(x, y)) {
                return 1; // Угловая фишка стабильна
            }
        }
        return 0; // Угловая фишка не стабильна
    }

    private int checkAdjacentToCorner(BoardService board, int x, int y, int playerId) {
        int stableCount = 0;

        // Проверяем клетки рядом с углом
        int[][] adjacentCells = {
                {x, y + 1}, // Вправо
                {x + 1, y}, // Вниз
                {x + 1, y + 1}, // Вправо и вниз
                {x, y - 1}, // Влево
                {x - 1, y}, // Вверх
                {x - 1, y - 1} // Влево и вверх
        };

        for (int[] cell : adjacentCells) {
            int adjX = cell[0];
            int adjY = cell[1];
            if(playerId == 1) {
                if (isInBounds(adjX, adjY) && board.hasPieceBlack(adjX, adjY)) {
                    stableCount += 1; // Соседняя фишка стабильна
                }
            } else if (playerId == 2) {
                if (isInBounds(adjX, adjY) && board.hasPieceWhite(adjX, adjY)) {
                    stableCount += 1;
                }
            }

        }

        return stableCount;
    }

    private boolean isInBounds(int x, int y) {
        // Проверяем, находится ли клетка в пределах доски
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    private double evaluateEdges(BoardService board, int currentPlayerId) {
        double edgeScore = 0;
        // Добавьте очки за контроль краев
        for (int i = 0; i < 8; i++) {
            if(currentPlayerId == 1){
                if (board.hasPieceBlack(0, i)) edgeScore += 5;  // Верхний край
                if (board.hasPieceBlack(7, i)) edgeScore += 5;  // Нижний край
                if (board.hasPieceBlack(i, 0)) edgeScore += 5;  // Левый край
                if (board.hasPieceBlack(i, 7)) edgeScore += 5;  // Правый край
            } else {
                if (board.hasPieceWhite(0, i)) edgeScore += 5;  // Верхний край
                if (board.hasPieceWhite(7, i)) edgeScore += 5;  // Нижний край
                if (board.hasPieceWhite(i, 0)) edgeScore += 5;  // Левый край
                if (board.hasPieceWhite(i, 7)) edgeScore += 5;  // Правый край
            }
        }
        return edgeScore;
    }

    private double evaluateCorners(BoardService board, int currentPlayerId) {
        double cornerScore = 0;
        // Добавьте очки за контроль углов
        if(currentPlayerId == 1){
            if (board.hasPieceBlack(0, 0)) cornerScore += 100;  // Верхний край
            if (board.hasPieceBlack(7, 0)) cornerScore += 100;  // Нижний край
            if (board.hasPieceBlack(0, 7)) cornerScore += 100;  // Левый край
            if (board.hasPieceBlack(7, 7)) cornerScore += 100;  // Правый край
        } else {
            if (board.hasPieceWhite(0, 0)) cornerScore += 100;  // Верхний край
            if (board.hasPieceWhite(7, 0)) cornerScore += 100;  // Нижний край
            if (board.hasPieceWhite(0, 7)) cornerScore += 100;  // Левый край
            if (board.hasPieceWhite(7, 7)) cornerScore += 100;  // Правый край
        }
        return cornerScore;
    }
}