package net.coderodde.jgs.model;

public abstract class AbstractHeuristicFunction<T extends AbstractNode<T>, W> {

    public abstract W evaluate(final T tail, final T head);
}
