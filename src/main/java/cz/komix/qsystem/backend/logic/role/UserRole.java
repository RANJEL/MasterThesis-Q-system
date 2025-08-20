package cz.komix.qsystem.backend.logic.role;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.persistence.config.serializationtoml.authorization.SerializedUserRole;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class that contains basic and cached information about user role.
 *
 * @author Jan Lejnar
 */
public class UserRole implements Comparable<UserRole> {

    private String id;
    private String name;
    private String description;
    private Set<QueryForm> allowedQueryForms;
    private QueryForm queryFormIntersection; // optimization

    /**
     * Creates user role, that doesn't allow any query form.
     *
     * @param name
     * @param description
     */
    public UserRole(String name, String description) {
        this(name, description, new HashSet<>());
    }

    /**
     * Creates user role, that allows defined query forms.
     *
     * @param name
     * @param allowedQueryForms defined query forms
     */
    public UserRole(String name, Set<QueryForm> allowedQueryForms) {
        this(name, "", allowedQueryForms);
    }

    public UserRole(String name, String description, Set<QueryForm> allowedQueryForms) {
        this.id = URLEncoder.encode(name.replaceAll(" ", "_"), StandardCharsets.UTF_8);
        this.name = name;
        this.description = description;
        this.allowedQueryForms = allowedQueryForms;
        updateQueryFormIntersection();
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

    public Set<QueryForm> getAllowedQueryForms() {
        return allowedQueryForms;
    }

    public QueryForm getQueryFormIntersection() {
        return queryFormIntersection;
    }

    public void updateQueryFormIntersection() {
        this.queryFormIntersection = QueryForm.mergeQueryForms(allowedQueryForms.toArray(QueryForm[]::new));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("UserRole{" +
                "name='" + name + '\'' +
                ", allowedQueryForms=");

        allowedQueryForms.forEach(queryForm -> stringBuilder.append(queryForm.getName() + ", "));
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return name.equals(userRole.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(UserRole o) {
        return this.name.compareTo(o.name);
    }

    public SerializedUserRole serialize() {
        SerializedUserRole serializedUserRole = new SerializedUserRole(this.getName(), this.getDescription());
        this.getAllowedQueryForms().forEach(allowedQueryForm -> serializedUserRole.allowQueryForm(allowedQueryForm.getName()));
        return serializedUserRole;
    }

}
