package model;

import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.LongAdder;

public class Stats {
    private LongAdder count = new LongAdder();
    private DoubleAccumulator sum = new DoubleAccumulator(Double::sum, 0.0);
    private DoubleAccumulator min = new DoubleAccumulator(Math::min, Double.POSITIVE_INFINITY);
    private DoubleAccumulator max = new DoubleAccumulator(Math::max, Double.NEGATIVE_INFINITY);

    public long getCount() {
        return count.sum();
    }

    public double getSum() {
        return sum.get();
    }

    public double getMin() {
        return min.get();
    }

    public double getMax() {
        return max.get();
    }

    public Stats accept(double value) {
        count.increment();
        sum.accumulate(value);
        min.accumulate(value);
        max.accumulate(value);
        return this;
    }

    public double average() {
        return count.sum() == 0 ? 0.0 : sum.get() / count.sum();
    }

    public void merge(Stats other) {
        this.count.add(other.getCount());
        this.sum.accumulate(other.getSum());
        this.min.accumulate(other.getMin());
        this.max.accumulate(other.getMax());
    }
}
