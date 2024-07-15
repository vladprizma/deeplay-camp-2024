package entity;

public class Board {
    private long blackChips;
    private long whiteChips;
    private long blackValidMoves;
    private long whiteValidMoves;

    public Board() {
        // Начальное расположение
        blackChips = 0x0000000810000000L;
        whiteChips = 0x0000001008000000L;
    }

    //!! Установка фишки на доску
    public void setPiece(int x, int y, int player) {
        long piece = 1L << (x + 8 * y);
        if(hasPiece(x, y)){
            removePiece(x, y);
        }
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
        long addMove = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (hasPiece(x, y)) {
                    if ((blackChips & (1L << (x + 8 * y))) != 0) {
                        // Для черных
                        // Горизонтали
                        for(int i = x - 1; i > 0; i --){
                            if((whiteChips & (1L << (i + 8 * y))) != 0){
                                addMove = 1L << (i - 1 + 8 * y);
                            } else {
                                addMoveMethod(addMove, 1);
                                break;
                            }
                        }
                        for(int i = x + 1; i < 7; i ++){
                            if((whiteChips & (1L << (i + 8 * y))) != 0){
                                addMove = 1L << (i + 1 + 8 * y);
                            } else {
                                addMoveMethod(addMove, 1);
                                break;
                            }
                        }
                        // Вертикали
                        for(int i = y - 1; i > 0; i --){
                            if((whiteChips & (1L << (x + 8 * i))) != 0){
                                addMove = 1L << (x + 8 * (i - 1));
                            } else {
                                addMoveMethod(addMove, 1);
                                break;
                            }
                        }
                        for(int i = y + 1; i < 7; i ++){
                            if((whiteChips & (1L << (x + 8 * i))) != 0){
                                addMove = 1L << (x + 8 * (i + 1));
                            } else {
                                addMoveMethod(addMove, 1);
                                break;
                            }
                        }
                        // Диагонали
                        int i = x - 1;
                        for(int j = y - 1; j > 0; j --){
                            if((whiteChips & (1L << (i + 8 * j))) != 0){
                                addMove = 1L << (i - 1 + 8 * (j - 1));
                                i --;
                            } else {
                                addMoveMethod(addMove, 1);
                                break;
                            }
                        }
                        i = x - 1;
                        for(int j = y + 1; j < 7; j ++){
                            if((whiteChips & (1L << (i + 8 * j))) != 0){
                                addMove = 1L << (i - 1 + 8 * (j + 1));
                                i--;
                            } else {
                                addMoveMethod(addMove, 1);
                                break;
                            }
                        }
                        i = x + 1;
                        for(int j = y - 1; j > 0; j --){
                            if((whiteChips & (1L << (i + 8 * j))) != 0){
                                addMove = 1L << (i + 1 + 8 * (j - 1));
                                i++;
                            } else {
                                addMoveMethod(addMove, 1);
                                break;
                            }
                        }
                        i = x + 1;
                        for(int j = y + 1; i < 7; j ++){
                            if((whiteChips & (1L << (i + 8 * j))) != 0){
                                addMove = 1L << (i + 1 + 8 * (j + 1));
                                i++;
                            } else {
                                addMoveMethod(addMove, 1);
                                break;
                            }
                        }
                    } else {
                        // Для белых
                        // Горизонтали
                        for(int i = x - 1; i > 0; i --){
                            if((blackChips & (1L << (i + 8 * y))) != 0){
                                addMove = 1L << (i - 1 + 8 * y);
                            } else {
                                addMoveMethod(addMove, 2);
                                break;
                            }
                        }
                        for(int i = x + 1; i < 7; i ++){
                            if((blackChips & (1L << (i + 8 * y))) != 0){
                                addMove = 1L << (i + 1 + 8 * y);
                            } else {
                                addMoveMethod(addMove, 2);
                                break;
                            }
                        }
                        // Вертикали
                        for(int i = y - 1; i > 0; i --){
                            if((blackChips & (1L << (x + 8 * i))) != 0){
                                addMove = 1L << (x + 8 * (i - 1));
                            } else {
                                addMoveMethod(addMove, 2);
                                break;
                            }
                        }
                        for(int i = y + 1; i < 7; i ++){
                            if((blackChips & (1L << (x + 8 * i))) != 0){
                                addMove = 1L << (x + 8 * (i + 1));
                            } else {
                                addMoveMethod(addMove, 2);
                                break;
                            }
                        }
                        // Диагонали
                        int i = x - 1;
                        for(int j = y - 1; j > 0; j --){
                            if((blackChips & (1L << (i + 8 * j))) != 0){
                                addMove = 1L << (i - 1 + 8 * (j - 1));
                                i--;
                            } else{
                                addMoveMethod(addMove, 2);
                                break;
                            }
                        }
                        i = x - 1;
                        for(int j = y + 1; j < 7; j ++){
                            if((blackChips & (1L << (i + 8 * j))) != 0){
                                addMove = 1L << (i - 1 + 8 * (j + 1));
                                i--;
                            } else{
                                addMoveMethod(addMove, 2);
                                break;
                            }
                        }
                        i = x + 1;
                        for(int j = y - 1; j > 0; j --){
                            if((blackChips & (1L << (i + 8 * j))) != 0){
                                addMove = 1L << (i + 1 + 8 * (j - 1));
                                i++;
                            } else{
                                addMoveMethod(addMove, 2);
                                break;
                            }
                        }
                        i = x + 1;
                        for(int j = y + 1; i < 7; j ++){
                            if((blackChips & (1L << (i + 8 * j))) != 0){
                                addMove = 1L << (i + 1 + 8 * (j + 1));
                                i++;
                            } else {
                                addMoveMethod(addMove, 2);
                            }
                        }
                    }
                }
            }
        }
        blackValidMoves = blackValidMoves & ~allChips;
        whiteValidMoves = whiteValidMoves & ~allChips;
    }

    public void addMoveMethod(long addMove, int chipsType){
        if(addMove != 0){
            if(chipsType == 1){
                blackValidMoves |= addMove;
                addMove = 0;
            }else{
                whiteValidMoves |= addMove;
                addMove = 0;
            }
        }
    }

    // Вызов доски
    public StringBuilder getBoardState(int player) {
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
        return state;
    }
}
