package io.deeplay.camp.bot;

import java.io.IOException;
import java.util.List;

import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.entity.Tile;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.DropoutLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.deeplearning4j.util.ModelSerializer;

import java.util.Random;

/**
 * Represents a Deep Q-Learning Agent that uses a neural network model
 * to make decisions based on input states and actions.
 * This agent employs an epsilon-greedy policy for action selection
 * and utilizes experience replay with a replay buffer.
 */
public class DeepQLearningAgent {

    private MultiLayerNetwork model;  // The neural network model used for Q-learning
    private double epsilon;  // Exploration rate for the epsilon-greedy policy
    private double gamma;  // Discount factor for future rewards
    private double alpha;  // Learning rate for the optimizer
    private Random random;
    private final ReplayBuffer replayBuffer;  // Buffer for storing past experiences
    private final DataBase experienceDatabase;  // Database for storing experiences
    private final int batchSize;  // Number of experiences to sample during training
    private double minEpsilon = 0.1;  // Minimum value for epsilon
    private double decayRate = 0.9999;  // Rate of decay for epsilon

    /**
     * Initializes the Deep Q-Learning Agent with default parameters and sets up
     * the replay buffer and experience database. It also initializes the neural
     * network model and loads experiences from the database into the buffer.
     */
    public DeepQLearningAgent() {
        this.epsilon = 0.1;
        this.gamma = 0.99;
        this.alpha = 0.01;
        this.random = new Random();
        this.batchSize = 64;
        this.replayBuffer = new ReplayBuffer(100000);
        this.experienceDatabase = new DataBase();

        List<Experience> experiences = experienceDatabase.getAllExperiences();
        for (Experience experience : experiences) {
            replayBuffer.add(experience);
        }

        initModel();
    }

    /**
     * Saves the current state of the neural network model to a specified file path.
     *
     * @param filePath the path to the file where the model will be saved
     */
    public void saveModel(String filePath) {
        try {
            ModelSerializer.writeModel(model, filePath, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a neural network model from the specified file path. If loading fails,
     * it initializes a new model.
     *
     * @param filePath the path to the file from which the model will be loaded
     */
    public void loadModel(String filePath) {
        try {
            model = ModelSerializer.restoreMultiLayerNetwork(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            initModel();
        }
    }

    /**
     * Initializes the neural network model with a predefined architecture and configuration.
     * The model consists of multiple dense layers and uses stochastic gradient descent
     * for optimization.
     */
    private void initModel() {
        int inputSize = 64;
        int outputSize = 64;

        NeuralNetConfiguration.ListBuilder builder = new NeuralNetConfiguration.Builder()
                .seed(7997)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(alpha))
                .weightInit(WeightInit.XAVIER)
                .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue)
                .gradientNormalizationThreshold(1.0)
                .list();

        builder.layer(new DenseLayer.Builder().nIn(inputSize).nOut(256)
                .activation(Activation.RELU)
                .build());

        builder.layer(new DenseLayer.Builder().nIn(256).nOut(256)
                .activation(Activation.RELU)
                .build());

        builder.layer(new DenseLayer.Builder().nIn(256).nOut(128)
                .activation(Activation.RELU)
                .build());

        builder.layer(new DenseLayer.Builder().nIn(128).nOut(64)
                .activation(Activation.RELU)
                .build());

        builder.layer(new OutputLayer.Builder(LossFunctions.LossFunction.L2)
                .activation(Activation.TANH)
                .nIn(64).nOut(outputSize)
                .build());

        model = new MultiLayerNetwork(builder.build());
        model.init();
    }

    /**
     * Updates the exploration rate (epsilon) based on the number of iterations
     * the model has undergone, applying a decay strategy to reduce epsilon over time.
     *
     * @return the updated epsilon value
     */
    private double updateEpsilon() {
        int currentIteration = model.getIterationCount();
        return Math.max(minEpsilon, Math.pow(decayRate, currentIteration));
    }

    /**
     * Selects an action for the current player based on the current game state.
     * Uses epsilon-greedy strategy for exploration vs. exploitation decisions.
     *
     * @param game the current state of the board
     * @param currentPlayer the player who is to make the next move
     * @return the selected Tile for the next action, or null if no valid moves are available
     */
    public Tile selectAction(BoardService game, int currentPlayer) {
        double currentEpsilon = updateEpsilon();
        if (random.nextDouble() < currentEpsilon) {
            List<Tile> validMoves = game.getAllValidTiles(currentPlayer);
            if (validMoves != null && !validMoves.isEmpty()) {
                return validMoves.get(random.nextInt(validMoves.size()));
            } else {
                return null;
            }
        } else {
            INDArray input = gameToINDArray(game);
            INDArray output = model.output(input);
            return getBestMove(output, game, currentPlayer);
        }
    }

    /**
     * Stores a new experience in the replay buffer and the experience database.
     *
     * @param game the current state of the board before the action
     * @param action the action taken by the agent, represented as an array
     * @param reward the reward received after taking the action
     * @param nextGame the state of the board after the action
     * @param done a boolean indicating whether the episode has terminated
     */
    public void storeExperience(BoardService game, int[] action, double reward, BoardService nextGame, boolean done) {
        INDArray state = gameToINDArray(game);
        INDArray nextState = gameToINDArray(nextGame);
        Experience experience = new Experience(state, action[0] * 8 + action[1], reward, nextState, done, 1.0);
        replayBuffer.add(experience);
        experienceDatabase.addExperience(experience);
    }

    /**
     * Converts the board state into an INDArray format suitable for input into
     * a neural network. The board state is transformed into a flat array where
     * each position can have a value representing the presence of a piece from either player.
     *
     * @param game the current state of the board
     * @return an INDArray representing the board state for input to the neural network
     */
    private INDArray gameToINDArray(BoardService game) {
        double[] flatBoard = new double[64];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                flatBoard[i * 8 + j] = game.hasPieceBlack(i, j) ? 1 : game.hasPieceWhite(i, j) ? -1 : 0;
            }
        }
        return Nd4j.create(flatBoard, new int[]{1, 64});
    }

    /**
     * Determines the best possible move based on the neural network's output for the given state.
     *
     * @param output the neural network's output representing action-value estimates
     * @param game the current state of the board
     * @param currentPlayer the player who is making the move
     * @return the best move for the current player
     */
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

    /**
     * Updates the discount factor (gamma) in a dynamic manner based on the current training iteration
     * count and the agent's win rate, adjusting gamma to balance between exploitation and exploration.
     *
     * @param iterationCount the number of training iterations completed
     * @param winRate the current win rate of the agent across episodes
     * @return the updated value for the discount factor gamma
     */
    private double updateGamma(int iterationCount, double winRate) {
        double iterationMaxGamma = 0.5;
        double finalMaxGamma = 0.9;
        double initialGamma = 0.1;

        double gammaBasedOnIterations = initialGamma + ((iterationMaxGamma - initialGamma) * Math.min(1.0, iterationCount / 100000.0));

        if (gammaBasedOnIterations >= 0.5) {
            double gammaAdj = (winRate - 0.5) * (finalMaxGamma - iterationMaxGamma);
            double gammaBasedOnPerf = iterationMaxGamma + gammaAdj;
            return Math.min(finalMaxGamma, Math.max(iterationMaxGamma, gammaBasedOnPerf));
        }

        return gammaBasedOnIterations;
    }

    /**
     * Samples a set of experiences from the replay buffer and uses them to update the neural network model.
     * The experiences are used to calculate target values, and the network is trained with these targets
     * for the selected batch of experiences.
     *
     * @param batchSize the number of experiences to sample and use for training
     */
    public void replay(int batchSize) {
        experienceDatabase.calculateAndStoreWinRate();
        double winRate = experienceDatabase.getLatestWinRate();
        if (winRate == -1.0) {
            winRate = 0.0;
        }
        gamma = updateGamma(model.getIterationCount(), winRate);

        List<Experience> experiences = replayBuffer.sample(batchSize);
        for (Experience experience : experiences) {
            INDArray state = experience.getState();
            int action = experience.getAction();
            double reward = experience.getReward();
            INDArray nextState = experience.getNextState();
            boolean done = experience.isDone();

            INDArray target = model.output(state);
            double targetValue = reward;
            if (!done) {
                targetValue += gamma * model.output(nextState).max(1).getDouble(0);
            }

            double tdError = Math.abs(targetValue - target.getDouble(action));
            experience.setPriority(tdError);
            replayBuffer.updatePriority(experience, tdError);

            target.putScalar(action, targetValue);

            double importanceWeight = experience.getImportanceWeight();
            INDArray importanceWeightedTarget = target.mul(importanceWeight);

            model.fit(new DataSet(state, importanceWeightedTarget));
        }
    }

    /**
     * Manages the process of storing an experience and carrying out training using
     * stored experiences once the replay buffer has a sufficient number of experiences.
     *
     * @param game the current state of the board before the action
     * @param action the action taken by the agent, represented as an array
     * @param reward the reward received after taking the action
     * @param nextGame the state of the board after the action
     * @param done a boolean indicating whether the episode has terminated
     */
    public void train(BoardService game, int[] action, double reward, BoardService nextGame, boolean done) {
        storeExperience(game, action, reward, nextGame, done);
        if (replayBuffer.size() >= batchSize) {
            replay(batchSize);
        }
    }
}