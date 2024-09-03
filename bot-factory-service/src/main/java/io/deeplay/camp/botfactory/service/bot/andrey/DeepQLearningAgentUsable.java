package io.deeplay.camp.botfactory.service.bot.andrey;

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.List;

public class DeepQLearningAgentUsable {
    private MultiLayerNetwork model;

    public DeepQLearningAgentUsable(String filePath) {
        loadModel(filePath);
    }

    public void loadModel(String filePath) {
        try {
            model = ModelSerializer.restoreMultiLayerNetwork(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tile selectAction(BoardService game, int currentPlayer) {
        INDArray input = gameToINDArray(game);
        INDArray output = model.output(input);
        return getBestMove(output, game, currentPlayer);
    }

    private INDArray gameToINDArray(BoardService game) {
        double[] flatBoard = new double[64];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                flatBoard[i * 8 + j] = game.hasPieceBlack(i, j) ? 1 : game.hasPieceWhite(i, j) ? -1 : 0;
            }
        }
        return Nd4j.create(flatBoard, new int[]{1, 64});
    }

    private Tile getBestMove(INDArray output, BoardService game, int currentPlayer) {
        List<Tile> validMoves = game.getAllValidTiles(currentPlayer);
        Tile bestMove = null;
        double bestValue = Double.NEGATIVE_INFINITY;

        for (Tile move : validMoves) {
            int index = move.getX() * 8 + move.getY();
            double value = output.getDouble(index);
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        return bestMove;
    }
}