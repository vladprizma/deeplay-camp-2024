package io.deeplay.camp.bot;

import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.entity.GameFinished;
import io.deeplay.camp.entity.Tile;

public class TreeBuilder {

    public static class Stats {

        int numNodes;             // Количество вершин
        int numTerminalNodes;     // Количество терминальных вершин
        public int maxDepth;             // Максимальная глубина дерева
        double coefBranch;        // Коэффициент ветвления
        long worktime;            // Время сбора статистики


        // Конструктор
        public Stats() {
            numNodes = 0;
            numTerminalNodes = 0;
            maxDepth = 0;
            coefBranch = 0.0;
            worktime = 0L;
        }

    }

    public static Stats buildGameTree(final BoardService root, int maxDepth, int playerId) {
        Stats stats = new Stats();
        long startTime = System.currentTimeMillis();

        traverse(root, stats, 0, maxDepth);

        stats.worktime = System.currentTimeMillis() - startTime;
        stats.coefBranch = (double) stats.numNodes / Math.max(1, stats.maxDepth); // Избегаем деления на ноль

        return stats;
    }

    private static void traverse(BoardService board, Stats stats, int depth, int maxDepth) {
        stats.numNodes++; // Увеличиваем количество вершин
        stats.maxDepth = Math.max(stats.maxDepth, depth);

        if (board.checkForWin().isGameFinished() || depth >= maxDepth) {
            if (board.checkForWin().isGameFinished()) {
                stats.numTerminalNodes++; // Увеличиваем количество терминальных узлов
            }
            return; // Завершаем обход
        }

        // Получение всех допустимых полей для текущего состояния
        for (Tile tile : board.getAllValidTileNoPlayer()) {
            BoardService child = board.getCopy(); // Клонируем текущее состояние
            child.makeMoveNoPlayer(tile); // Делаем ход
            traverse(child, stats, depth + 1, maxDepth); // Увеличиваем глубину
        }
    }

}