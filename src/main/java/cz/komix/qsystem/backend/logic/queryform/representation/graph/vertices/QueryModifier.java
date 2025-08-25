package cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices;

/**
 * @author Jan Lejnar
 */
public class QueryModifier extends QFVertex {
    public QueryModifier(String name) {
        super(name);
    }

    @Override
    public void addToCategorizedStorage() {
        itsStorage.getQueryModifierVertices().add(this);
    }

    @Override
    public boolean isVertexOfCategory(QFVertexCategoriesEnum category) {
        return category.equals(QFVertexCategoriesEnum.QUERY_MODIFIER);
    }
}
