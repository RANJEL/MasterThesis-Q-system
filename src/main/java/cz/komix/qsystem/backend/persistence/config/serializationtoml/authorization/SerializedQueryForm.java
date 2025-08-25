package cz.komix.qsystem.backend.persistence.config.serializationtoml.authorization;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.logic.queryform.element.ElementaryQueryForm;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryEntityType;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryModifier;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class that handles deserialization of query forms based on authorization configuration file into {@link QueryForm}
 *
 * @author Jan Lejnar
 */
public class SerializedQueryForm implements Comparable<SerializedQueryForm>, Serializable {
    private String name;
    private String description;
    private List<String> elementaryQueryForms;

    public SerializedQueryForm(String name, String description) {
        this.name = name;
        this.description = description;
        this.elementaryQueryForms = new ArrayList<>();
    }

    public void addElementaryQueryForm(String elementaryQueryForm) {
        elementaryQueryForms.add(elementaryQueryForm);
    }

    public QueryForm deserialize() {
        QueryForm queryForm = new QueryForm(name, description);

        elementaryQueryForms.forEach(serializedElementaryQueryForm -> {
            String[] parts = serializedElementaryQueryForm.split(" & ");
            if (parts.length != 3) {
                throw new IllegalStateException();
            }
            String queryEntityType = parts[0];
            String queryModifier = parts[1];
            String queryType = parts[2];

            ElementaryQueryForm elementaryQueryForm = new ElementaryQueryForm(
                    new QueryEntityType(queryEntityType),
                    new QueryModifier(queryModifier),
                    new QueryType(queryType));
            queryForm.addElementaryQueryForm(elementaryQueryForm);
        });

        return queryForm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerializedQueryForm that = (SerializedQueryForm) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(SerializedQueryForm o) {
        return this.name.compareTo(o.name);
    }
}
