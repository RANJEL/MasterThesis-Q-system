package cz.komix.qsystem.backend.logic.queryform.element;

import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryEntityType;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryModifier;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryType;
import lombok.Data;

/**
 * Representation of elementary query form, e.g.: single_category:person {@literal &} fuzzy_text {@literal &} standard_query
 *
 * @author Jan Lejnar
 */
@Data
public class ElementaryQueryForm implements Comparable<ElementaryQueryForm> {

    private QueryEntityType queryEntityType;
    private QueryModifier queryModifier;
    private QueryType queryType;

    public ElementaryQueryForm(QueryEntityType queryEntityType, QueryModifier queryModifier, QueryType queryType) {
        this.queryEntityType = queryEntityType;
        this.queryModifier = queryModifier;
        this.queryType = queryType;
    }

    @Override
    public String toString() {
        return queryEntityType + " & " + queryModifier + " & " + queryType;
    }

    @Override
    public int compareTo(ElementaryQueryForm o) {
        if (this.queryEntityType.equals(o.queryEntityType)) {
            if (this.queryModifier.equals(o.queryModifier)) {
                return this.queryType.compareTo(o.queryType);
            } else {
                return this.queryModifier.compareTo(o.queryModifier);
            }
        } else {
            return this.queryEntityType.compareTo(o.queryEntityType);
        }
    }
}
