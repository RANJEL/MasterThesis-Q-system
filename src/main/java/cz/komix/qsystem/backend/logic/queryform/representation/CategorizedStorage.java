package cz.komix.qsystem.backend.logic.queryform.representation;

import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryEntityType;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryModifier;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryType;

import java.util.HashSet;
import java.util.Set;

/**
 * We have graph representation but we also need to know set of all vertices and categorize,
 * which one is QueryType/QueryModifier/QueryEntityType
 *
 * @author Jan Lejnar
 */
public class CategorizedStorage {

    private Set<QueryType> queryTypeVertices;
    private Set<QueryModifier> queryModifierVertices;
    private Set<QueryEntityType> queryEntityTypeVertices;

    public CategorizedStorage() {
        queryTypeVertices = new HashSet<>();
        queryModifierVertices = new HashSet<>();
        queryEntityTypeVertices = new HashSet<>();
    }

    public Set<QueryType> getQueryTypeVertices() {
        return queryTypeVertices;
    }

    public void setQueryTypeVertices(Set<QueryType> queryTypeVertices) {
        this.queryTypeVertices = queryTypeVertices;
    }

    public Set<QueryModifier> getQueryModifierVertices() {
        return queryModifierVertices;
    }

    public void setQueryModifierVertices(Set<QueryModifier> queryModifierVertices) {
        this.queryModifierVertices = queryModifierVertices;
    }

    public Set<QueryEntityType> getQueryEntityTypeVertices() {
        return queryEntityTypeVertices;
    }

    public void setQueryEntityTypeVertices(Set<QueryEntityType> queryEntityTypeVertices) {
        this.queryEntityTypeVertices = queryEntityTypeVertices;
    }
}
