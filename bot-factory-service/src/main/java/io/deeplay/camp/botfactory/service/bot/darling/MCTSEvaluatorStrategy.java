package io.deeplay.camp.botfactory.service.bot.darling;

import io.deeplay.camp.botfactory.model.MCTSNode;
import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;

import java.util.List;
import java.util.Random;

public class MCTSEvaluatorStrategy implements EvaluationStrategy {

    private static final int SIMULATION_COUNT = 10;
    private static final double EXPLORATION_PARAMETER = Math.sqrt(2);

    @Override
    public double evaluate(BoardService boardService, int currentPlayerId) {
        MCTSNode root = new MCTSNode(boardService, currentPlayerId, null);

        for (int i = 0; i < SIMULATION_COUNT; i++) {
            MCTSNode selectedNode = select(root);
            MCTSNode expandedNode = expand(selectedNode);

            double reward = expandedNode.isTerminal() ? simulate(selectedNode) : simulate(expandedNode);
            backpropagate(expandedNode, reward);
        }

        MCTSNode bestChild = root.getBestChild();
        if (bestChild == null) {
            return 0.0;
        }

        return bestChild.getWinRate();
    }

    private MCTSNode select(MCTSNode node) {
        while (!node.isLeaf()) {
            node = node.getBestUCTChild(EXPLORATION_PARAMETER);
        }
        return node;
    }

    private MCTSNode expand(MCTSNode node) {
        if (!node.isTerminal()) {
            node.expand();
        }

        return node.isLeaf() ? node : node.getRandomChild();
    }

    private double simulate(MCTSNode node) {
        BoardService board = node.getBoardService().getBoardServiceCopy();
        int currentPlayer = node.getCurrentPlayerId();
        Random random = new Random();

        while (!board.checkForWin().isGameFinished()) {
            List<Tile> legalMoves = board.getAllValidTiles(currentPlayer);
            if (legalMoves.isEmpty()) {
                currentPlayer = 3 - currentPlayer;
                continue;
            }
            Tile move = legalMoves.get(random.nextInt(legalMoves.size()));
            board.makeMove(currentPlayer, move);
            currentPlayer = 3 - currentPlayer;
        }

        return board.checkForWin().getUserIdWinner() == node.getCurrentPlayerId() ? 1.0 : 0.0;
    }

    private void backpropagate(MCTSNode node, double reward) {
        while (node != null) {
            node.update(reward);
            node = node.getParent();
        }
    }
}
