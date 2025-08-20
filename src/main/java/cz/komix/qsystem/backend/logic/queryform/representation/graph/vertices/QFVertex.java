package cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices;

import cz.komix.qsystem.backend.logic.queryform.representation.CategorizedStorage;

import java.util.Objects;

/**
 * @author Jan Lejnar
 */
public abstract class QFVertex implements Comparable<QFVertex> {

    protected String name;
    protected CategorizedStorage itsStorage;

    public QFVertex(String name) {
        this.name = name;
        this.itsStorage = null;
    }

    public String getName() {
        return name;
    }

    public void setItsStorage(CategorizedStorage itsStorage) {
        this.itsStorage = itsStorage;
    }

    public abstract void addToCategorizedStorage();

    public abstract boolean isVertexOfCategory(QFVertexCategoriesEnum category);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QFVertex qfVertex = (QFVertex) o;
        return name.equals(qfVertex.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(QFVertex o) {
        return this.name.compareTo(o.name);
    }
}
