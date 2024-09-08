package io.deeplay.camp.bot;

import org.nd4j.linalg.api.ndarray.INDArray;

public class Experience {
    private final INDArray state;
    private final int action;
    private final double reward;
    private final INDArray nextState;
    private final boolean done;
    private double priority;
    private double importanceWeight;

    /**
     * Constructs an instance of an {@code Experience} with specified parameters.
     *
     * @param state  The current state of the environment.
     * @param action  The action taken by the agent.
     * @param reward  The reward received after taking the action.
     * @param nextState  The next state of the environment after the action.
     * @param done  A boolean indicating if the episode is done.
     * @param priority  The initial priority of this experience.
     */
    public Experience(INDArray state, int action, double reward, INDArray nextState, boolean done, double priority) {
        this.state = state;
        this.action = action;
        this.reward = reward;
        this.nextState = nextState;
        this.done = done;
        this.priority = priority;
        this.importanceWeight = 1.0;
    }

    public double getImportanceWeight() {
        return importanceWeight;
    }

    public void setImportanceWeight(double importanceWeight) {
        this.importanceWeight = importanceWeight;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public INDArray getState() {
        return state;
    }

    public int getAction() {
        return action;
    }

    public double getReward() {
        return reward;
    }

    public INDArray getNextState() {
        return nextState;
    }

    public boolean isDone() {
        return done;
    }
}