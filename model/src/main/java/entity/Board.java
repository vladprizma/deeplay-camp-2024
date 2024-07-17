package entity;

public class Board {
    private final long blackChips;
    private final long whiteChips;

    public Board() {
        // Начальное расположение
        blackChips = 0x0000000810000000L;
        whiteChips = 0x0000001008000000L;
    }

    public long getBlackChips() {
        return blackChips;
    }

    public long getWhiteChips() {
        return whiteChips;
    }

    public String boardToClient(){
        StringBuilder state = new StringBuilder("");
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((blackChips & (1L << (x + 8 * y))) != 0) {
                    state.append("X ");
                } else if ((whiteChips & (1L << (x + 8 * y))) != 0) {
                    state.append("0 ");
                } else state.append(". ");
            }
        }
        return "Board{" + state.toString() + '}';
    }
}