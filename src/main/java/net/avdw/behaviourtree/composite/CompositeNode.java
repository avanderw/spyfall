package net.avdw.behaviourtree.composite;

import net.avdw.behaviourtree.Node;

import java.util.List;

/**
 * One parent
 * One or more children
 * Controls flow
 */
public abstract class CompositeNode implements Node {
    private final List<Node> children;

    protected CompositeNode(final List<Node> children) {
        this.children = children;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    protected List<Node> getChildren() {
        return children;
    }
}
