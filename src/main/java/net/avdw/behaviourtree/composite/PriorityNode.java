package net.avdw.behaviourtree.composite;

import net.avdw.behaviourtree.Node;
import net.avdw.behaviourtree.NodeStatus;

import java.util.List;

/**
 * ticks its children sequentially until one of them returns SUCCESS, RUNNING or ERROR.
 * If all children return the failure state, the priority also returns FAILURE
 */
public class PriorityNode extends CompositeNode {
    public PriorityNode(final List<Node> children) {
        super(children);
    }

    @Override
    public NodeStatus tick() {
        for (Node child : getChildren()) {
            NodeStatus status = child.tick();
            switch (status) {
                case SUCCESS:
                case RUNNING:
                    return status;
                case FAILURE:
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
        return NodeStatus.FAILURE;
    }
}
