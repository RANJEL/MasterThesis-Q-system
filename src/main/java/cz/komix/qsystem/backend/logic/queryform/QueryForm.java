package cz.komix.qsystem.backend.logic.queryform;


import cz.komix.qsystem.backend.logic.queryform.element.ElementaryQueryForm;
import cz.komix.qsystem.backend.logic.queryform.representation.CategorizedStorage;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.QFEdge;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.*;
import cz.komix.qsystem.backend.persistence.config.serializationtoml.authorization.SerializedQueryForm;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.Multigraph;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Query Form internal representation (Tripartite Graph).
 * Note: Does not allow nodes with same name! ={@literal >} EntityType = person {@literal &}{@literal &} Modifier = person
 *
 * @author Jan Lejnar
 */
public class QueryForm implements Comparable<QueryForm> {
    private String id;
    private String name;
    private String description;
    private Graph<QFVertex, QFEdge> queryFormGraph;
    private CategorizedStorage categorizedStorage;

    /**
     * Creates empty query form == that allows nothing.
     * In order to permit something, you need to call addElementaryQueryForm.
     *
     * @param name
     */
    public QueryForm(String name) {
        this(name, "");
    }

    public QueryForm(String name, String description) {
        this.id = URLEncoder.encode(name.replaceAll(" ", "_"), StandardCharsets.UTF_8);
        this.name = name;
        this.description = description;
        this.queryFormGraph = new Multigraph<>(QFEdge.class);
        this.categorizedStorage = new CategorizedStorage();
    }

    public QueryForm(QueryForm from) {
        this.id = from.id;
        this.name = from.name;
        this.description = from.description;
        /* creates this.queryFormGraph and this.categorizedStorage */
        from.getAllElementaryQueryForms().forEach(this::addElementaryQueryForm);
    }

    public static QueryForm mergeQueryForms(QueryForm... queryForms) {
        QueryForm mergedQueryForm = new QueryForm("mergedQueryForm");

        for (QueryForm queryForm :
                queryForms) {
            if (queryForm != null) {
                Graphs.addGraph(mergedQueryForm.queryFormGraph, queryForm.queryFormGraph);

                mergedQueryForm.categorizedStorage.getQueryTypeVertices().addAll(queryForm.categorizedStorage.getQueryTypeVertices());
                mergedQueryForm.categorizedStorage.getQueryModifierVertices().addAll(queryForm.categorizedStorage.getQueryModifierVertices());
                mergedQueryForm.categorizedStorage.getQueryEntityTypeVertices().addAll(queryForm.categorizedStorage.getQueryEntityTypeVertices());
            }
        }

        return mergedQueryForm;
    }

    /**
     * Adds 3 relations into graph.
     *
     * @param elementaryQueryForm
     */
    public void addElementaryQueryForm(ElementaryQueryForm elementaryQueryForm) {
        QueryEntityType queryEntityTypeVertex = elementaryQueryForm.getQueryEntityType();
        QueryModifier queryModifierVertex = elementaryQueryForm.getQueryModifier();
        QueryType queryTypeVertex = elementaryQueryForm.getQueryType();

        queryEntityTypeVertex.setItsStorage(categorizedStorage);
        queryModifierVertex.setItsStorage(categorizedStorage);
        queryTypeVertex.setItsStorage(categorizedStorage);

        addRelationEntityType2Modifier(queryEntityTypeVertex, queryModifierVertex);
        addRelationModifier2Type(queryModifierVertex, queryTypeVertex);
        addRelationEntityType2Type(queryEntityTypeVertex, queryTypeVertex);
    }

    public Set<QueryType> getQueryTypeVertices() {
        return categorizedStorage.getQueryTypeVertices();
    }

    public Set<QueryModifier> getQueryModifierVertices() {
        return categorizedStorage.getQueryModifierVertices();
    }

    public Set<QueryEntityType> getQueryEntityTypeVertices() {
        return categorizedStorage.getQueryEntityTypeVertices();
    }

    public Set<QFVertex> getConnectedVerticesOfSpecificType(QFVertex startingVertex, QFVertexCategoriesEnum vertexCategoryToReturn) {
        if (!queryFormGraph.containsVertex(startingVertex)) {
            return new HashSet<>();
        }
        return queryFormGraph.edgesOf(startingVertex).stream()
                .filter(edge -> edge.hasVertexWithSpecificCategory(vertexCategoryToReturn))
                .map(edge -> edge.getVertexWithSpecificCategory(vertexCategoryToReturn))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public Set<QFVertex> getConnectedVerticesOfSpecificTypeWithConnectionToSpecificVertex(
            QFVertex startingVertex, QFVertexCategoriesEnum vertexCategoryToReturn, QFVertex specificVertex) {
        return getConnectedVerticesOfSpecificType(startingVertex, vertexCategoryToReturn).stream()
                .filter(connectedVertex -> {
                    if (!queryFormGraph.containsVertex(connectedVertex) || !queryFormGraph.containsVertex(specificVertex)) {
                        return false;
                    }
                    return queryFormGraph.containsEdge(connectedVertex, specificVertex);
                })
                .collect(Collectors.toSet());
    }

    public List<ElementaryQueryForm> getAllElementaryQueryForms() {
        List<ElementaryQueryForm> elementaryQueryFormList = new ArrayList<>();

        getQueryEntityTypeVertices()
                .forEach(entityType -> getConnectedVerticesOfSpecificType(entityType, QFVertexCategoriesEnum.QUERY_MODIFIER)
                        .forEach(modifier -> getConnectedVerticesOfSpecificTypeWithConnectionToSpecificVertex(modifier, QFVertexCategoriesEnum.QUERY_TYPE, entityType)
                                .forEach(type -> elementaryQueryFormList.add(new ElementaryQueryForm(entityType, (QueryModifier) modifier, (QueryType) type)))));
        return elementaryQueryFormList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryForm queryForm = (QueryForm) o;
        return this.toString().equals(queryForm.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(QueryForm o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        List<QFVertex> queryEntityTypeVertices = new ArrayList<>(getQueryEntityTypeVertices());
        Collections.sort(queryEntityTypeVertices);
        List<QFVertex> queryModifierVertices = new ArrayList<>(getQueryModifierVertices());
        Collections.sort(queryModifierVertices);
        List<QFVertex> queryTypeVertices = new ArrayList<>(getQueryTypeVertices());
        Collections.sort(queryTypeVertices);

        List<ElementaryQueryForm> elementaryQueryForms = getAllElementaryQueryForms();
        Collections.sort(elementaryQueryForms);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("QueryForm{" +
                "name='" + name + '\'' +
                ", queryEntityTypeVertices=" + queryEntityTypeVertices +
                ", queryModifierVertices=" + queryModifierVertices +
                ", queryTypeVertices=" + queryTypeVertices +
                ", queryFormGraph=");

        elementaryQueryForms.forEach(elementaryForm -> stringBuilder.append("\n" + elementaryForm));

        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }

    public SerializedQueryForm serialize() {
        SerializedQueryForm serializedQueryForm = new SerializedQueryForm(this.getName(), this.getDescription());
        this.getAllElementaryQueryForms()
                .forEach(elementaryQueryForm -> serializedQueryForm.addElementaryQueryForm(elementaryQueryForm.toString()));
        return serializedQueryForm;
    }

    private void addRelationEntityType2Modifier(QueryEntityType queryEntityType, QueryModifier queryModifier) {
        addVertexIfDontExist(queryEntityType);
        addVertexIfDontExist(queryModifier);
        queryFormGraph.addEdge(queryEntityType, queryModifier);
    }

    private void addRelationEntityType2Type(QueryEntityType queryEntityType, QueryType queryType) {
        addVertexIfDontExist(queryEntityType);
        addVertexIfDontExist(queryType);
        queryFormGraph.addEdge(queryEntityType, queryType);
    }

    private void addRelationModifier2Type(QueryModifier queryModifier, QueryType queryType) {
        addVertexIfDontExist(queryModifier);
        addVertexIfDontExist(queryType);
        queryFormGraph.addEdge(queryModifier, queryType);
    }

    private void addVertexIfDontExist(QFVertex vertex) {
        if (!queryFormGraph.containsVertex(vertex)) {
            queryFormGraph.addVertex(vertex);
            vertex.addToCategorizedStorage();
        }
    }
}
