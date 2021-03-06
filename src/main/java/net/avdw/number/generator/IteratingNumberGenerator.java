package net.avdw.number.generator;

import com.google.inject.Inject;

import java.util.List;

public class IteratingNumberGenerator implements NumberGenerator {
    private int currentIdx = -1;
    private int direction = 1;
    private final boolean wrap;
    private final List<Double> numbers;

    @Inject
    public IteratingNumberGenerator(final List<Double> numbers, final boolean wrap) {
        this.numbers = numbers;
        this.wrap = wrap;
    }

    /**
     * Generate a value.
     *
     * @return a value following the implementation rule
     */
    @Override
    public Double nextValue() {
        currentIdx += direction;

        if (currentIdx < 0 || currentIdx == numbers.size()) {
            if (wrap) {
                currentIdx %= numbers.size();
            } else {
                direction *= -1;
                currentIdx += 2 * direction;
            }
        }

        return numbers.get(currentIdx);
    }
}
