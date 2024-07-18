package entity;

public class Board {
    private long blackChips;
    private long whiteChips;

    public Board() {
        // Начальное расположение
        blackChips = 0x0000000810000000L;
        whiteChips = 0x0000001008000000L;
    }

    public void setBlackChips(long blackChips) {
        this.blackChips = blackChips;
    }

    public void setWhiteChips(long whiteChips) {
        this.whiteChips = whiteChips;
    }

    public long getBlackChips() {
        return blackChips;
    }

    public long getWhiteChips() {
        return whiteChips;
    }
}