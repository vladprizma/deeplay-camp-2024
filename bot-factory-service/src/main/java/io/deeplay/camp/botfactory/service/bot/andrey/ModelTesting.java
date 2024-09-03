package io.deeplay.camp.botfactory.service.bot.andrey;

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;
import io.deeplay.camp.botfactory.service.bot.BotStrategy;

import java.util.List;

public class ModelTesting extends BotStrategy {
    private final DeepQLearningAgentUsable agent;
    private final UtilityFunction utilityFunction;
    private final boolean train;

    public ModelTesting(int id, String name, DeepQLearningAgentUsable agent, boolean train) {
        super(id, name);
        this.agent = agent;
        this.utilityFunction = new AdvancedUtilityFunction();
        this.train = train;
    }

    @Override
    public Tile getMove(int currentPlayerId, BoardService boardLogic) {
            return getMakeMoveWithoutTraining(currentPlayerId, boardLogic);
    }

    public Tile getMakeMoveWithoutTraining(int currentPlayerId, BoardService boardLogic) {
        BoardService boardCopy = getBoardCopy(boardLogic);
        Tile selectedMove = selectBestMove(boardCopy, currentPlayerId);
        if (selectedMove == null)
            return null;

        return selectedMove;
    }

    private Tile selectBestMove(BoardService board, int currentPlayerId) {
        return agent.selectAction(board, currentPlayerId);
    }

    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }

    public BoardService getBoardCopy(BoardService boardService) {
        return boardService.getCopy();
    }

}
