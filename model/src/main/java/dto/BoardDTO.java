package dto;
import entity.Board;

public class BoardDTO {
    private Board board;
    private long blackChips;
    private long whiteChips;

    public BoardDTO(Board board) {
        this.board = new Board();
        this.blackChips = board.getBlackChips();
        this.whiteChips = board.getWhiteChips();
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
        return "Board{" + state + '}';
    }
}
