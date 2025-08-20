package cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices;

/**
 * @author Jan Lejnar
 */
public class QueryEntityType extends QFVertex {
    public QueryEntityType(String name) {
        super(name);
    }

    @Override
    public void addToCategorizedStorage() {
        itsStorage.getQueryEntityTypeVertices().add(this);
    }

    @Override
    public boolean isVertexOfCategory(QFVertexCategoriesEnum category) {
        return category.equals(QFVertexCategoriesEnum.QUERY_ENTITY_TYPE);
    }
}
