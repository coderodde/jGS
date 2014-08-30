package net.coderodde.jgs.model.support;

import net.coderodde.jgs.model.Weight;

public class IntegerWeight extends Weight<Integer> {

    @Override
    public Integer identity() {
        return 0;
    }

    @Override
    public Integer append(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer largest() {
        return Integer.MAX_VALUE;
    }
}
