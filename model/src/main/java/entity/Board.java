package entity;

public class Board {
    private long blackChips;
    private long whiteChips;

    public Board() {
        // Начальное расположение
        blackChips = 0x0000000810000000L;
        whiteChips = 0x0000001008000000L;
    }

    //!! Установка фишки на доску
    public void setPiece(int x, int y, int player) {
        long piece = 1L << (x + 8 * y);
        if (player == 1) {
            blackChips |= piece;
        } else if (player == 2) {
            whiteChips |= piece;
        }
    }

    //!! Убрать фишку
    public void removePiece(int x, int y) {
        long mask = ~(1L << (x + 8 * y));
        blackChips &= mask;
        whiteChips &= mask;
    }

    // Проверка на наличие фишки
    public boolean hasPiece(int x, int y) {
        long mask = 1L << (x + 8 * y);
        return ((blackChips & mask) != 0) || ((whiteChips & mask) != 0);
    }

    // Проверка на возможность хода


    public boolean isValidMove(int x, int y, int player) {
        if (hasPiece(x, y)) {
            return false; // Поле уже занято фишкой
        }

        long piece = 1L << (x + 8 * y);
        long opponentChips = (player == 1) ? whiteChips : blackChips;

        boolean validMove = false;

        // Логика проверки возможности хода
        // Здесь нужно реализовать логику проверки возможности хода для игры reversi

        return validMove;
    }

    // Получение возможных вариантов

    public long getValidMoves(int player) {
        long validMoves = 0x0000000000000000L;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (isValidMove(x, y, player)) {
                    validMoves |= 1L << (x + 8 * y);
                }
            }
        }

        return validMoves;
    }

    // Вызов доски
    public StringBuilder getBoardState() {
        StringBuilder state = new StringBuilder("");
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (hasPiece(x, y)) {
                    if ((blackChips & (1L << (x + 8 * y))) != 0) {
                        state.append("X ");
                    } else {
                        state.append("0 ");
                    }
                } else {
                    state.append(". ");
                }
            }
            state.append("\n");
        }
        return state;
    }
}
