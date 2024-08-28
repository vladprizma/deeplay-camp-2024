package io.deeplay.camp.bot;

import io.deeplay.camp.board.BoardService;

import java.util.Random;

public class RandomUtilityFunction implements UtilityFunction {

    private final Random random;

    public RandomUtilityFunction() {
        this.random = new Random();
    }

    @Override
    public double evaluate(final BoardService board, int currentPlayerId) {
        // Проверяем состояние игры
        int opponentPlayer = currentPlayerId == 1 ? 2 : 1;
        if (board.checkForWin().isGameFinished()) {
            if (board.checkForWin().getUserIdWinner() == currentPlayerId) {
                return 1; // Первый игрок выиграл
            } else if (board.checkForWin().getUserIdWinner() == opponentPlayer) {
                return -1; // Второй игрок выиграл
            } else {
                return 0; // Ничья
            }
        } else {
            // Возвращаем случайное значение в диапазоне от -1 до 1
            return random.nextDouble() * 2 - 1; // Генерируем значение от -1 до 1
        }
    }
}