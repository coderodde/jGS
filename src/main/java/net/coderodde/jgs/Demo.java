package net.coderodde.jgs;

import net.coderodde.jgs.support.DecreaseKeySuite;
import net.coderodde.jgs.support.HeapDemoSuite;

public class Demo {

    public static void main(final String... args) {
        new HeapDemoSuite().run();
        new DecreaseKeySuite().run();
    }
}
