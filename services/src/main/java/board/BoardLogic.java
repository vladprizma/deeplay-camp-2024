package board;

import entity.Board;

public class BoardLogic {

    private Board board;
    private long blackChips;
    private long whiteChips;
    private long blackValidMoves;
    private long whiteValidMoves;

    public BoardLogic(Board board) {
        this.board = new Board();
        this.blackChips = board.getBlackChips();
        this.whiteChips = board.getWhiteChips();
    }

    //!! Установка фишки на доску
    public void setPiece(int x, int y, int player) {
        long piece = 1L << (x + 8 * y);
        long tempChips;
        if(isValidMove(x, y, player)){
            if (player == 1) {
                blackChips |= piece;
                // Горизонтали
                tempChips = 0;
                for (int i = x - 1; i > 0; i--) {
                    if ((whiteChips & (1L << (i + 8 * y))) != 0) {
                        tempChips |= (1L << (i + 8 * y));
                    } else if((blackChips & (1L << (i + 8 * y))) != 0){
                        whiteChips &= ~tempChips;
                        blackChips |= tempChips;
                    } else break;
                }
                tempChips = 0;
                for (int i = x + 1; i < 7; i++) {
                    if ((whiteChips & (1L << (i + 8 * y))) != 0) {
                        tempChips |= (1L << (i + 8 * y));
                    } else if((blackChips & (1L << (i + 8 * y))) != 0){
                        whiteChips &= ~tempChips;
                        blackChips |= tempChips;
                    } else break;
                }
                // Вертикали
                tempChips = 0;
                for (int i = y - 1; i > 0; i--) {
                    if ((whiteChips & (1L << (x + 8 * i))) != 0) {
                        tempChips |= (1L << (x + 8 * i));
                    } else if((blackChips & (1L << (x + 8 * i))) != 0){
                        whiteChips &= ~tempChips;
                        blackChips |= tempChips;
                    } else break;
                }
                tempChips = 0;
                for (int i = y + 1; i < 7; i++) {
                    if ((whiteChips & (1L << (x + 8 * i))) != 0) {
                        tempChips |= (1L << (x + 8 * i));
                    } else if((blackChips & (1L << (x + 8 * i))) != 0){
                        whiteChips &= ~tempChips;
                        blackChips |= tempChips;
                    } else break;
                }
                // Диагонали
                tempChips = 0;
                int i = x - 1;
                for (int j = y - 1; j > 0; j--) {
                    if ((whiteChips & (1L << (i + 8 * j))) != 0) {
                        tempChips |= (1L << (i + 8 * j));
                        i--;
                    } else if((blackChips & (1L << (i + 8 * j))) != 0){
                        whiteChips &= ~tempChips;
                        blackChips |= tempChips;
                    } else break;
                }
                tempChips = 0;
                i = x - 1;
                for (int j = y + 1; j < 7; j++) {
                    if ((whiteChips & (1L << (i + 8 * j))) != 0) {
                        tempChips |= (1L << (i + 8 * j));
                        i--;
                    } else if((blackChips & (1L << (i + 8 * j))) != 0){
                        whiteChips &= ~tempChips;
                        blackChips |= tempChips;
                    } else break;
                }
                tempChips = 0;
                i = x + 1;
                for (int j = y - 1; j > 0; j--) {
                    if ((whiteChips & (1L << (i + 8 * j))) != 0) {
                        tempChips |= (1L << (i + 8 * j));
                        i++;
                    } else if((blackChips & (1L << (i + 8 * j))) != 0){
                        whiteChips &= ~tempChips;
                        blackChips |= tempChips;
                    } else break;
                }
                tempChips = 0;
                i = x + 1;
                for (int j = y + 1; j < 7; j++) {
                    if ((whiteChips & (1L << (i + 8 * j))) != 0) {
                        tempChips |= (1L << (i + 8 * j));
                        i++;
                    } else if((blackChips & (1L << (i + 8 * j))) != 0){
                        whiteChips &= ~tempChips;
                        blackChips |= tempChips;
                    } else break;
                }
            } else if (player == 2) {
                whiteChips |= piece;
                // Горизонтали
                tempChips = 0;
                for (int i = x - 1; i > 0; i--) {
                    if ((blackChips & (1L << (i + 8 * y))) != 0) {
                        tempChips |= (1L << (i + 8 * y));
                    } else if((whiteChips & (1L << (i + 8 * y))) != 0){
                        blackChips &= ~tempChips;
                        whiteChips |= tempChips;
                    } else break;
                }
                tempChips = 0;
                for (int i = x + 1; i < 7; i++) {
                    if ((blackChips & (1L << (i + 8 * y))) != 0) {
                        tempChips |= (1L << (i + 8 * y));
                    } else if((whiteChips & (1L << (i + 8 * y))) != 0){
                        blackChips &= ~tempChips;
                        whiteChips |= tempChips;
                    } else break;
                }
                // Вертикали
                tempChips = 0;
                for (int i = y - 1; i > 0; i--) {
                    if ((blackChips & (1L << (x + 8 * i))) != 0) {
                        tempChips |= (1L << (x + 8 * i));
                    } else if((whiteChips & (1L << (x + 8 * i))) != 0){
                        blackChips &= ~tempChips;
                        whiteChips |= tempChips;
                    } else break;
                }
                tempChips = 0;
                for (int i = y + 1; i < 7; i++) {
                    if ((blackChips & (1L << (x + 8 * i))) != 0) {
                        tempChips |= (1L << (x + 8 * i));
                    } else if((whiteChips & (1L << (x + 8 * i))) != 0){
                        blackChips &= ~tempChips;
                        whiteChips |= tempChips;
                    } else break;
                }
                // Диагонали
                tempChips = 0;
                int i = x - 1;
                for (int j = y - 1; j > 0; j--) {
                    if ((blackChips & (1L << (i + 8 * j))) != 0) {
                        tempChips |= (1L << (i + 8 * j));
                        i--;
                    } else if((whiteChips & (1L << (i + 8 * j))) != 0){
                        blackChips &= ~tempChips;
                        whiteChips |= tempChips;
                    } else break;
                }
                tempChips = 0;
                i = x - 1;
                for (int j = y + 1; j < 7; j++) {
                    if ((blackChips & (1L << (i + 8 * j))) != 0) {
                        tempChips |= (1L << (i + 8 * j));
                        i--;
                    } else if((whiteChips & (1L << (i + 8 * j))) != 0){
                        blackChips &= ~tempChips;
                        whiteChips |= tempChips;
                    } else break;
                }
                tempChips = 0;
                i = x + 1;
                for (int j = y - 1; j > 0; j--) {
                    if ((blackChips & (1L << (i + 8 * j))) != 0) {
                        tempChips |= (1L << (i + 8 * j));
                        i++;
                    } else if((whiteChips & (1L << (i + 8 * j))) != 0){
                        blackChips &= ~tempChips;
                        whiteChips |= tempChips;
                    } else break;
                }
                tempChips = 0;
                i = x + 1;
                for (int j = y + 1; j < 7; j++) {
                    if ((blackChips & (1L << (i + 8 * j))) != 0) {
                        tempChips |= (1L << (i + 8 * j));
                        i++;
                    } else if((whiteChips & (1L << (i + 8 * j))) != 0){
                        blackChips &= ~tempChips;
                        whiteChips |= tempChips;
                    } else break;
                }
                whiteChips |= piece;
            }
        } else {
            System.out.println("Unexpected move");
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
        if (player == 1) {
            if((blackValidMoves & (1L << (x + 8 * y))) != 0) {
                return true;
            } else return false;
        } else if (player == 2) {
            if((whiteValidMoves & (1L << (x + 8 * y))) != 0) {
                return true;
            } else return false;
        } else return false;
    }

    // Получение возможных вариантов
    public long getValidMoves(int player) {
        long validMoves = 0x0000000000000000L;
        if (player == 1) {
            validMoves |= blackValidMoves;
        } else if (player == 2) {
            validMoves |= whiteValidMoves;
        }
        return validMoves;
    }

    // Счёт игры
    public int[] score() {
        int[] score = new int[2];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                long mask = 1L << (x + 8 * y);
                if ((blackChips & mask) != 0) {
                    score[0]++;
                } else if ((whiteChips & mask) != 0) {
                    score[1]++;
                }
            }
        }
        return score;
    }

    // Создание возможных ходов
    public void createValidMoves() {
        long allChips = blackChips | whiteChips;
        blackValidMoves = 0;
        whiteValidMoves = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (hasPiece(x, y)) {
                    if ((blackChips & (1L << (x + 8 * y))) != 0) {
                        // Для черных
                        // Горизонтали
                        for (int i = x - 1; i > 0; i--) {
                            if ((whiteChips & (1L << (i + 8 * y))) != 0) {
                                if (!hasPiece(i - 1, y)) {
                                    blackValidMoves |= (1L << (i - 1 + 8 * y));
                                }
                            } else break;
                        }
                        for (int i = x + 1; i < 7; i++) {
                            if ((whiteChips & (1L << (i + 8 * y))) != 0) {
                                if (!hasPiece(i + 1, y)) {
                                    blackValidMoves |= (1L << (i + 1 + 8 * y));
                                }
                            } else break;
                        }
                        // Вертикали
                        for (int i = y - 1; i > 0; i--) {
                            if ((whiteChips & (1L << (x + 8 * i))) != 0) {
                                if (!hasPiece(x, i - 1)) {
                                    blackValidMoves |= (1L << (x + 8 * (i - 1)));
                                }
                            } else break;
                        }
                        for (int i = y + 1; i < 7; i++) {
                            if ((whiteChips & (1L << (x + 8 * i))) != 0) {
                                if (!hasPiece(x, i + 1)) {
                                    blackValidMoves |= (1L << (x + 8 * (i + 1)));
                                }
                            } else break;
                        }
                        // Диагонали
                        int i = x - 1;
                        for (int j = y - 1; j > 0; j--) {
                            if ((whiteChips & (1L << (i + 8 * j))) != 0) {
                                if (!hasPiece(i - 1, j - 1)) {
                                    blackValidMoves |= (1L << (i - 1 + 8 * (j - 1)));
                                }
                                i--;
                            } else break;
                        }
                        i = x - 1;
                        for (int j = y + 1; j < 7; j++) {
                            if ((whiteChips & (1L << (i + 8 * j))) != 0) {
                                if (!hasPiece(i - 1, j - 1)) {
                                    blackValidMoves |= (1L << (i - 1 + 8 * (j - 1)));
                                }
                                i--;
                            } else break;
                        }
                        i = x + 1;
                        for (int j = y - 1; j > 0; j--) {
                            if ((whiteChips & (1L << (i + 8 * j))) != 0) {
                                if (!hasPiece(i + 1, j - 1)) {
                                    blackValidMoves |= (1L << (i + 1 + 8 * (j - 1)));
                                }
                                i++;
                            } else break;
                        }
                        i = x + 1;
                        for (int j = y + 1; i < 7; j++) {
                            if ((whiteChips & (1L << (i + 8 * j))) != 0) {
                                if (!hasPiece(i + 1, j + 1)) {
                                    blackValidMoves |= (1L << (i + 1 + 8 * (j + 1)));
                                }
                                i++;
                            } else break;
                        }
                    } else {
                        // Для белых
                        // Горизонтали
                        for (int i = x - 1; i > 0; i--) {
                            if ((blackChips & (1L << (i + 8 * y))) != 0) {
                                if (!hasPiece(i - 1, y)) {
                                    whiteValidMoves |= (1L << (i - 1 + 8 * y));
                                }
                            } else break;
                        }
                        for (int i = x + 1; i < 7; i++) {
                            if ((blackChips & (1L << (i + 8 * y))) != 0) {
                                if (!hasPiece(i + 1, y)) {
                                    whiteValidMoves |= (1L << (i + 1 + 8 * y));
                                }
                            } else break;
                        }
                        // Вертикали
                        for (int i = y - 1; i > 0; i--) {
                            if ((blackChips & (1L << (x + 8 * i))) != 0) {
                                if (!hasPiece(x, i - 1)) {
                                    whiteValidMoves |= (1L << (x + 8 * (i - 1)));
                                }
                            } else break;
                        }
                        for (int i = y + 1; i < 7; i++) {
                            if ((blackChips & (1L << (x + 8 * i))) != 0) {
                                if (!hasPiece(x, i + 1)) {
                                    whiteValidMoves |= (1L << (x + 8 * (i + 1)));
                                }
                            } else break;
                        }
                        // Диагонали
                        int i = x - 1;
                        for (int j = y - 1; j > 0; j--) {
                            if ((blackChips & (1L << (i + 8 * j))) != 0) {
                                if (!hasPiece(i - 1, j - 1)) {
                                    whiteValidMoves |= (1L << (i - 1 + 8 * (j - 1)));
                                }
                                i--;
                            } else break;
                        }
                        i = x - 1;
                        for (int j = y + 1; j < 7; j++) {
                            if ((blackChips & (1L << (i + 8 * j))) != 0) {
                                if (!hasPiece(i - 1, j + 1)) {
                                    whiteValidMoves |= (1L << (i - 1 + 8 * (j + 1)));
                                }
                                i--;
                            } else break;
                        }
                        i = x + 1;
                        for (int j = y - 1; j > 0; j--) {
                            if ((blackChips & (1L << (i + 8 * j))) != 0) {
                                if (!hasPiece(i + 1, j - 1)) {
                                    whiteValidMoves |= (1L << (i + 1 + 8 * (j - 1)));
                                }
                                i++;
                            } else break;
                        }
                        i = x + 1;
                        for (int j = y + 1; i < 7; j++) {
                            if ((blackChips & (1L << (i + 8 * j))) != 0) {
                                if (!hasPiece(i + 1, j + 1)) {
                                    whiteValidMoves |= (1L << (i + 1 + 8 * (j + 1)));
                                }
                                i++;
                            } else break;
                        }
                    }
                }
            }
        }
        blackValidMoves = blackValidMoves & ~allChips;
        whiteValidMoves = whiteValidMoves & ~allChips;
    }

    public long getBlackValidMoves() {
        createValidMoves();
        return blackValidMoves;
    }

    public long getWhiteValidMoves() {
        createValidMoves();
        return whiteValidMoves;
    }

    // Вызов доски
    public StringBuilder getBoardState(int player) {
        createValidMoves();
        StringBuilder state = new StringBuilder("");
        state.append("\n\n");
        for (int y = 0; y < 8; y++) {
            state.append((8 - y) + " ");
            for (int x = 0; x < 8; x++) {
                if (hasPiece(x, y)) {
                    if ((blackChips & (1L << (x + 8 * y))) != 0) {
                        state.append("X ");
                    } else {
                        state.append("0 ");
                    }
                } else {
                    long validMoves = (player == 1) ? blackValidMoves : whiteValidMoves;
                    long mask = 1L << (x + 8 * y);
                    if ((validMoves & mask) != 0) {
                        state.append("* ");
                    } else {
                        state.append(". ");
                    }
                }
            }
            state.append("\n");
        }
        state.append(" ");
        for (char i = 'a'; i <='h'; i++)
            state.append(" " + i);

        state.append("\n\n");
        return state;
    }
}