package io.deeplay.camp.botfactory.service.bot.andrey;


import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;
import io.deeplay.camp.botfactory.service.bot.BotStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class MCTSBot extends BotStrategy {
    private final int numSimulations;
    private final Random random;
    private static final Logger logger = Logger.getLogger(MCTSBot.class.getName());

    public MCTSBot(int id, String name, int numSimulations) {
        super(id, name);
        this.numSimulations = numSimulations;
        this.random = new Random();
    }

    @Override
    public Tile getMove(int currentPlayerId, BoardService boardLogic) {
        GameStateNode root = new GameStateNode(boardLogic.getCopy(), null, currentPlayerId, null);

        for (int i = 0; i < numSimulations; i++) {
            GameStateNode selectedNode = select(root);
            GameStateNode expandedNode = expand(selectedNode);
            int result = simulate(expandedNode);
            back(expandedNode, result);
        }

        GameStateNode bestChild = bestChild(root);
        return bestChild != null ? bestChild.getMove() : getFallbackMove(boardLogic, currentPlayerId);
    }

    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }

    private GameStateNode select(GameStateNode node) {
        while (!node.getChildren().isEmpty()) {
            node = bestUCT(node);
        }
        return node;
    }

    private GameStateNode expand(GameStateNode node) {
        if (!isTerminal(node) && node.getChildren().isEmpty()) {
            List<Tile> validMoves = node.getBoard().getAllValidTiles(node.getCurrentPlayer());
            for (Tile move : validMoves) {
                BoardService newBoard = node.getBoard().getCopy();
                newBoard.makeMove(node.getCurrentPlayer(), move);
                GameStateNode childNode = new GameStateNode(newBoard, move, getNextPlayer(node.getCurrentPlayer()), node);
                node.addChild(childNode);
            }
        }
        List<GameStateNode> childrenList = new ArrayList<>(node.getChildren());
        return childrenList.isEmpty() ? node : childrenList.get(random.nextInt(childrenList.size()));
    }

    private int simulate(GameStateNode node) {
        BoardService boardCopy = node.getBoard().getCopy();
        int currentPlayer = node.getCurrentPlayer();

        while (!boardCopy.checkForWin().isGameFinished()) {
            List<Tile> validMoves = boardCopy.getAllValidTiles(currentPlayer);
            if (validMoves.isEmpty()) {
                currentPlayer = getNextPlayer(currentPlayer);
                continue;
            }
            Tile randomMove = validMoves.get(random.nextInt(validMoves.size()));
            boardCopy.makeMove(currentPlayer, randomMove);
            currentPlayer = getNextPlayer(currentPlayer);
        }

        int winner = boardCopy.checkForWin().getUserIdWinner() == 1 ?
            1 : boardCopy.checkForWin().getUserIdWinner() == 2 ? 2 : 2;

        return winner;
    }

    private void back(GameStateNode node, int result) {
        while (node != null) {
            node.incrementVisits();
            if (result != node.getCurrentPlayer()) {
                node.incrementWins();
            }
            node = node.getParent();
        }
    }

    private GameStateNode bestUCT(GameStateNode node) {
        GameStateNode bestNode = null;
        double bestValue = Double.NEGATIVE_INFINITY;

        for (GameStateNode child : node.getChildren()) {
            double uctValue;
            if (child.getVisits() == 0) {
                uctValue = Double.POSITIVE_INFINITY;
            } else {
                uctValue = (child.getWins() / (double) child.getVisits())
                        + Math.sqrt(2) * Math.sqrt(Math.log(node.getVisits()) / (double) child.getVisits());
            }
            if (uctValue > bestValue) {
                bestValue = uctValue;
                bestNode = child;
            }
        }
        return bestNode;
    }

    private GameStateNode bestChild(GameStateNode node) {
        if (node.getChildren().isEmpty()) {
            return null;
        }

        GameStateNode bestNode = null;
        double bestValue = Double.NEGATIVE_INFINITY;

        for (GameStateNode child : node.getChildren()) {
            double winRate = child.getWins() / (double) child.getVisits();
            if (winRate > bestValue) {
                bestValue = winRate;
                bestNode = child;
            }
        }
        return bestNode;
    }

    private Tile getFallbackMove(BoardService boardLogic, int currentPlayerId) {
        List<Tile> validMoves = boardLogic.getAllValidTiles(currentPlayerId);
        return validMoves.isEmpty() ? null : validMoves.get(0);
    }

    private int getNextPlayer(int currentPlayerId) {
        return currentPlayerId == 1 ? 2 : 1;
    }

    private boolean isTerminal(GameStateNode node) {
        return node.getBoard().checkForWin().isGameFinished();
    }
}
