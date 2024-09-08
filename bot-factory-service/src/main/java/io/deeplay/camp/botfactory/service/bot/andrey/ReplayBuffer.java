package io.deeplay.camp.bot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ReplayBuffer {
    private final int capacity;
    private final LinkedList<Experience> buffer;
    private final Random random;
    private static final double PRIORITY_ALPHA_BASE  = 0.6;
    private static final double PRIORITY_BETA_BASE  = 0.4;
    private double maxPriority = 1.0;

    /**
     * Constructs a ReplayBuffer with a specified capacity where experiences will be stored.
     *
     * @param capacity the maximum number of experiences that can be stored in the buffer
     */
    public ReplayBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new LinkedList<>();
        this.random = new Random();
    }

    /**
     * Updates the priority of a specified experience within the buffer if it exists.
     *
     * <p>This method iterates over the buffer to find the matching experience
     * and updates its priority. The priority is indicative of the temporal-difference
     * error typically, reflecting the surprise or learning potential of this experience.
     *
     * @param experience the experience whose priority is to be updated
     * @param newPriority the new priority to assign to the experience
     */
    public void updatePriority(Experience experience, double newPriority) {
        for (Experience exp : buffer) {
            if (exp.equals(experience)) {
                exp.setPriority(newPriority);
                break;
            }
        }
    }

    /**
     * Adds a new experience to the buffer.
     *
     * <p>If the buffer is at maximum capacity, it removes the oldest experience to make
     * room for the new one. The new experience is assigned a priority equal to the
     * current maximum priority in the buffer, ensuring it has the highest chance
     * of being sampled in the near term.
     *
     * @param experience the experience to be added to the buffer
     */
    public void add(Experience experience) {
        if (buffer.size() >= capacity) {
            buffer.removeFirst();
        }
        experience.setPriority(maxPriority);
        buffer.add(experience);
    }

    /**
     * Samples a batch of experiences from the buffer, using priority-based sampling.
     *
     * <p>This method calculates priorities using the current proportionate alpha and beta
     * values, which depend on the size of the buffer. It randomly samples experiences
     * based on their priorities, normalizing these to retrieve a representative batch.
     *
     * <p>The sampled experiences are adjusted for their sampling probability by calculating
     * and setting an importance weight, which helps adjust updates during learning based on
     * each sample’s probability of being drawn.
     *
     * @param batchSize the number of experiences to sample from the buffer
     * @return a list of experiences sampled according to their priority-based probability
     */
    public List<Experience> sample(int batchSize) {
        double priorAlpha = proportionalAlpha(size());
        double priorBeta = proportionalBeta(size());

        double totalPriority = buffer.stream().mapToDouble(e -> Math.pow(e.getPriority(), priorAlpha)).sum();

        List<Experience> batch = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            double rand = random.nextDouble() * totalPriority;
            double cumulativePriority = 0;

            for (Experience experience : buffer) {
                cumulativePriority += Math.pow(experience.getPriority(), priorAlpha);
                if (cumulativePriority >= rand) {
                    batch.add(experience);
                    break;
                }
            }
        }

        double maxWeight = Math.pow(totalPriority / buffer.size(), -priorBeta);
        for (Experience exp : batch) {
            double samplingProb = Math.pow(exp.getPriority(), priorAlpha) / totalPriority;
            double weight = Math.pow(samplingProb * buffer.size(), -priorBeta) / maxWeight;
            exp.setImportanceWeight(weight);
        }

        return batch;
    }

    /**
     * Returns the current number of experiences stored in the buffer.
     *
     * @return the size of the buffer
     */
    public int size() {
        return buffer.size();
    }

    /**
     * Computes the proportionate alpha value for sampling from the buffer.
     *
     * <p>The alpha value is adjusted based on the current size of the buffer, allowing
     * the influence of priority to change as more experiences are added. The base value
     * is decrementing as the buffer size approaches its maximum observed capacity.
     *
     * @param size the current number of experiences in the buffer
     * @return the computed alpha value for priority sampling basis
     */
    private double proportionalAlpha(int size) {
        return PRIORITY_ALPHA_BASE - 0.3 * (Math.min(50000, size) / 50000.0);
    }

    /**
     * Computes the proportionate beta value for during sampling.
     *
     * <p>The beta value dictates how much the importance weights correct for non-uniform
     * sampling probabilities. This increases as the buffer grows, reinforcing proper learning
     * updates as more experiences become available.
     *
     * @param size the current number of experiences in the buffer
     * @return the computed beta value for weighting corrections
     */
    private double proportionalBeta(int size) {
        return PRIORITY_BETA_BASE + 0.5 * (Math.min(50000, size) / 50000.0);
    }
}