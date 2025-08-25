package cz.komix.qsystem.backend.logic.queryform.representation.graph;

import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QFVertex;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QFVertexCategoriesEnum;
import org.jgrapht.graph.DefaultEdge;

import java.util.Optional;

/**
 * @author Jan Lejnar
 */
public class QFEdge extends DefaultEdge {

    private static boolean isVertexWithSpecificCategory(QFVertex vertex, QFVertexCategoriesEnum category) {
        return vertex.isVertexOfCategory(category);
    }

    public boolean hasVertexWithSpecificCategory(QFVertexCategoriesEnum category) {
        QFVertex vertex1 = (QFVertex) this.getSource();
        QFVertex vertex2 = (QFVertex) this.getTarget();
        return isVertexWithSpecificCategory(vertex1, category) || isVertexWithSpecificCategory(vertex2, category);
    }

    public Optional<QFVertex> getVertexWithSpecificCategory(QFVertexCategoriesEnum category) {
        QFVertex vertex1 = (QFVertex) this.getSource();
        QFVertex vertex2 = (QFVertex) this.getTarget();

        if (isVertexWithSpecificCategory(vertex1, category)) {
            return Optional.of(vertex1);
        } else if (isVertexWithSpecificCategory(vertex2, category)) {
            return Optional.of(vertex2);
        } else {
            return Optional.empty();
        }
    }

    public boolean hasSpecificVertex(QFVertex vertex) {
        QFVertex vertex1 = (QFVertex) this.getSource();
        QFVertex vertex2 = (QFVertex) this.getTarget();
        return vertex.equals(vertex1) || vertex.equals(vertex2);
    }

}
