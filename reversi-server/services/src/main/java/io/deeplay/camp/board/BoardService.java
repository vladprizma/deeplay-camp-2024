package io.deeplay.camp.board;

import io.deeplay.camp.entity.Board;
import io.deeplay.camp.entity.GameFinished;
import io.deeplay.camp.entity.Tile;

import java.util.ArrayList;
import java.util.List;

public class BoardService {

    private final Board board;
    private long blackChips;
    private long whiteChips;
    private long blackValidMoves;
    private long whiteValidMoves;

    public BoardService(Board board) {
        this.board = board;
        this.blackChips = board.getBlackChips();
        this.whiteChips = board.getWhiteChips();
    }
    
    //!! Установка фишки на доску
    public void setPiece(int x, int y, int player) {
        long piece = 1L << (x + 8 * y);
        long tempChips;
        long currentChips; long invertedChips;
        int a; int b;
        boolean breakA; boolean breakB; boolean breakX;
        if (player == 1) {
            currentChips = blackChips;
            invertedChips = whiteChips;
        } else {
            currentChips = whiteChips;
            invertedChips = blackChips;
        }
        if (isValidMove(x, y, player)) {
            currentChips |= piece;
            // Горизонтали
            tempChips = 0;
            for (int i = x - 1; i > 0; i--) {
                if ((invertedChips & (1L << (i + 8 * y))) != 0) {
                    tempChips |= (1L << (i + 8 * y));
                } else if ((currentChips & (1L << (i + 8 * y))) != 0) {
                    invertedChips &= ~tempChips;
                    currentChips |= tempChips;
                } else break;
            }
            tempChips = 0;
            for (int i = x + 1; i < 7; i++) {
                if ((invertedChips & (1L << (i + 8 * y))) != 0) {
                    tempChips |= (1L << (i + 8 * y));
                } else if ((currentChips & (1L << (i + 8 * y))) != 0) {
                    invertedChips &= ~tempChips;
                    currentChips |= tempChips;
                } else break;
            }
            // Вертикали
            tempChips = 0;
            for (int j = y - 1; j > 0; j--) {
                if ((invertedChips & (1L << (x + 8 * j))) != 0) {
                    tempChips |= (1L << (x + 8 * j));
                } else if ((currentChips & (1L << (x + 8 * j))) != 0) {
                    invertedChips &= ~tempChips;
                    currentChips |= tempChips;
                } else break;
            }
            tempChips = 0;
            for (int j = y + 1; j < 7; j++) {
                if ((invertedChips & (1L << (x + 8 * j))) != 0) {
                    tempChips |= (1L << (x + 8 * j));
                } else if ((currentChips & (1L << (x + 8 * j))) != 0) {
                    invertedChips &= ~tempChips;
                    currentChips |= tempChips;
                } else break;
            }
            tempChips = 0; a = x - 1;
            // Диагонали
            for (int j = y - 1; j > 0; j--) {
                if ((invertedChips & (1L << (a + 8 * j))) != 0) {
                    tempChips |= (1L << (a + 8 * j));
                    a--;
                } else if ((currentChips & (1L << (a + 8 * j))) != 0) {
                    invertedChips &= ~tempChips;
                    currentChips |= tempChips;
                } else break;
            }
            tempChips = 0; b = x + 1;
            for (int j = y - 1; j > 0; j--) {
                if ((invertedChips & (1L << (b + 8 * j))) != 0) {
                    tempChips |= (1L << (b + 8 * j));
                    b++;
                } else if ((currentChips & (1L << (b + 8 * j))) != 0) {
                    invertedChips &= ~tempChips;
                    currentChips |= tempChips;
                } else break;
            }
            tempChips = 0; a = x - 1;
            for (int j = y + 1; j < 7; j++) {
                if ((invertedChips & (1L << (a + 8 * j))) != 0) {
                    tempChips |= (1L << (a + 8 * j));
                    a--;
                } else if ((currentChips & (1L << (a + 8 * j))) != 0) {
                    invertedChips &= ~tempChips;
                    currentChips |= tempChips;
                } else break;
            }
            tempChips = 0; b = x + 1;
            for (int j = y + 1; j < 7; j++) {
                if ((invertedChips & (1L << (b + 8 * j))) != 0) {
                    tempChips |= (1L << (b + 8 * j));
                    b++;
                } else if ((currentChips & (1L << (b + 8 * j))) != 0) {
                    invertedChips &= ~tempChips;
                    currentChips |= tempChips;
                } else break;
            }
            if (player == 1) {
                blackChips = currentChips;
                whiteChips = invertedChips;
                board.setWhiteChips(whiteChips);
                board.setBlackChips(blackChips);
            } else {
                whiteChips = currentChips;
                blackChips = invertedChips;
                board.setWhiteChips(whiteChips);
                board.setBlackChips(blackChips);
            }
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
    
    public Board getBoard() {
        return board;
    }

    // Проверка на возможность хода
    public boolean isValidMove(int x, int y, int player) {
        createValidMoves();
        if (player == 1) {
            return (blackValidMoves & (1L << (x + 8 * y))) != 0;
        } else if (player == 2) {
            return (whiteValidMoves & (1L << (x + 8 * y))) != 0;
        } else {
            return false;
        }
    }

    // Получение возможных вариантов
    public long getValidMoves(int player) {
        createValidMoves();
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
        long targetChip;
        long validMoves = 0;
        int a, b;
        boolean breakA, breakB, breakX;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (hasPiece(x, y)) {
                    if ((blackChips & (1L << (x + 8 * y))) != 0) targetChip = whiteChips;
                    else targetChip = blackChips;
                    for (int i = x - 1; i > 0; i--) {
                        if ((targetChip & (1L << (i + 8 * y))) != 0) {
                            if (!hasPiece(i - 1, y)) validMoves |= (1L << (i - 1 + 8 * y));
                        } else break;
                    }
                    for (int i = x + 1; i < 7; i++) {
                        if ((targetChip & (1L << (i + 8 * y))) != 0) {
                            if (!hasPiece(i + 1, y)) validMoves |= (1L << (i + 1 + 8 * y));
                        } else break;
                    }
                    a = x - 1; b = x + 1;
                    breakA = true; breakB = true; breakX = true;
                    for (int j = y - 1; j > 0; j--) {
                        if ((targetChip & (1L << (a + 8 * j))) != 0 && breakA) {
                            if (!hasPiece(a - 1, j - 1)) {
                                validMoves |= (1L << (a - 1 + 8 * (j - 1)));
                            }
                            a--;
                        } else breakA = false;
                        if ((targetChip & (1L << (b + 8 * j))) != 0 && breakB) {
                            if (!hasPiece(b + 1, j - 1)) {
                                validMoves |= (1L << (b + 1 + 8 * (j - 1)));
                            }
                            b++;
                        } else breakB = false;
                        if ((targetChip & (1L << (x + 8 * j))) != 0 && breakX) {
                            if (!hasPiece(x, j - 1)) {
                                validMoves |= (1L << (x + 8 * (j - 1)));
                            }
                        } else breakX = false;
                        if (!breakA && !breakB && !breakX) break;
                    }
                    a = x - 1; b = x + 1;
                    breakA = true; breakB = true; breakX = true;
                    for (int j = y + 1; j < 7; j++) {
                        if ((targetChip & (1L << (a + 8 * j))) != 0 && breakA) {
                            if (!hasPiece(a - 1, j + 1)) {
                                validMoves |= (1L << (a - 1 + 8 * (j + 1)));
                            }
                            a--;
                        } else breakA = false;
                        if ((targetChip & (1L << (b + 8 * j))) != 0 && breakB) {
                            if (!hasPiece(b + 1, j + 1)) {
                                validMoves |= (1L << (b + 1 + 8 * (j + 1)));
                            }
                            b++;
                        } else breakB = false;
                        if ((targetChip & (1L << (x + 8 * j))) != 0 && breakX) {
                            if (!hasPiece(x, j + 1)) {
                                validMoves |= (1L << (x + 8 * (j + 1)));
                            }
                        } else breakX = false;
                        if (!breakA && !breakB && !breakX) break;
                    }
                    if ((blackChips & (1L << (x + 8 * y))) != 0) blackValidMoves |= validMoves;
                    else whiteValidMoves |= validMoves;
                    validMoves = 0;
                }
            }
        }
        blackValidMoves &= ~allChips;
        whiteValidMoves &= ~allChips;
    }

    // Вызов доски
    public StringBuilder getBoardState(int player) {
        createValidMoves();
        StringBuilder state = new StringBuilder();
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

    public String getBoardStateDTO(int player){
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
        }

        return "Board{" + state + '}';
    }

    public long getBlackValidMoves() {
        createValidMoves();
        return blackValidMoves;
    }

    public long getWhiteValidMoves() {
        createValidMoves();
        return whiteValidMoves;
    }

    public List<Tile> getAllValidTiles(int playerId) {
        List<Tile> validTiles = new ArrayList<>();
        long validMoves = getValidMoves(playerId);
        for (int i = 0; i < 64; i++) {
            long mask = 1L << i;
            if ((validMoves & mask) != 0) {
                int x = i % 8;
                int y = i / 8;
                validTiles.add(new Tile(x, y));
            }
        }
        return validTiles;
    }

    public boolean makeMove(int playerId, Tile tile) {
        int x = tile.getX();
        int y = tile.getY();
        setPiece(x, y, playerId);
                
        return true;
    }

    public List<Tile> getChips(int playerId) {
        List<Tile> playerChips = new ArrayList<>();
        long chips;
        if(playerId == 1) {
            chips = blackChips;
        } else chips = whiteChips;
        for (int i = 0; i < 64; i++) {
            long mask = 1L << i;
            if ((chips & mask) != 0) {
                int x = i % 8;
                int y = i / 8;
                playerChips.add(new Tile(x, y));
            }
        }
        return playerChips;
    }

    public GameFinished checkForWin() {
        GameFinished gameFinished;
        int winner = 0;

        long blackValidMoves = getValidMoves(1);
        long whiteValidMoves = getValidMoves(2);
        
        
        //TODO переделать на && после починки логики игры
        if (blackValidMoves == 0 || whiteValidMoves == 0) {
            if (getChips(1).size() > getChips(2).size()) {
                winner = 1;
                return gameFinished = new GameFinished(true, winner);
            } else if (getChips(2).size() > getChips(1).size()) {
                winner = 2;
                return gameFinished = new GameFinished(true, winner);
            } else {
                winner = 3;
                return gameFinished = new GameFinished(true, winner);
            }
        }
        return new GameFinished(false, -1);
    }

    public void setBlackValidMoves(long blackValidMoves) {
        this.blackValidMoves = blackValidMoves;
    }

    public void setWhiteValidMoves(long whiteValidMoves) {
        this.whiteValidMoves = whiteValidMoves;
    }

    public long getBlackChips() {
        return blackChips;
    }

    public void setBlackChips(long blackChips) {
        this.blackChips = blackChips;
    }

    public long getWhiteChips() {
        return whiteChips;
    }

    public void setWhiteChips(long whiteChips) {
        this.whiteChips = whiteChips;
    }
}