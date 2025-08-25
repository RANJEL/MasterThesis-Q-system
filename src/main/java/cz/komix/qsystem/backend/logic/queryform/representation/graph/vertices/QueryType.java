package cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices;

/**
 * @author Jan Lejnar
 */
public class QueryType extends QFVertex {
    public QueryType(String name) {
        super(name);
    }

    @Override
    public void addToCategorizedStorage() {
        itsStorage.getQueryTypeVertices().add(this);
    }

    @Override
    public boolean isVertexOfCategory(QFVertexCategoriesEnum category) {
        return category.equals(QFVertexCategoriesEnum.QUERY_TYPE);
    }
}
