package cz.komix.qsystem.backend.persistence.config.serializationtoml.authorization;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.logic.role.UserRole;

import java.util.*;

/**
 * Class that handles deserialization of user roles based on authorization configuration file into {@link UserRole}
 *
 * @author Jan Lejnar
 */
public class SerializedUserRole implements Comparable<SerializedUserRole> {
    private String name;
    private String description;
    private List<String> queryForms;

    public SerializedUserRole(String name, String description) {
        this.name = name;
        this.description = description;
        this.queryForms = new ArrayList<>();
    }

    public void allowQueryForm(String queryForm) {
        queryForms.add(queryForm);
    }

    public UserRole deserialize(Map<String, QueryForm> queryFormMap) {
        Set<QueryForm> allowedQueryForms = new HashSet<>();
        queryForms.forEach(queryFormName -> {
            QueryForm queryForm = queryFormMap.get(queryFormName);
            if (queryForm != null) {
                allowedQueryForms.add(queryForm);
            }
        });

        return new UserRole(name, description, allowedQueryForms);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerializedUserRole that = (SerializedUserRole) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(SerializedUserRole o) {
        return this.name.compareTo(o.name);
    }
}
