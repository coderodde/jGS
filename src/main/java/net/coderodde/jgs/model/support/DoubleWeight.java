package net.coderodde.jgs.model.support;

import net.coderodde.jgs.model.Weight;

public class DoubleWeight extends Weight<Double> {

    @Override
    public Double identity() {
        return 0.0;
    }

    @Override
    public Double append(final Double a, final Double b) {
        return a + b;
    }
}
