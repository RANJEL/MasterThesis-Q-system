package cz.komix.qsystem.backend.persistence.config.serializationtoml.authorization;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.logic.role.UserRole;
import lombok.Data;

import java.util.*;

/**
 * Root class that is mapped to TOML structure of authorization config file {see authorization.toml}.
 *
 * @author Jan Lejnar
 */
@Data
public class AuthorizationConfDataClass {
    private Set<SerializedUserRole> definedUserRoles;
    private Set<SerializedQueryForm> definedQueryForms;

    public AuthorizationConfDataClass() {
        this.definedUserRoles = new TreeSet<>();
        this.definedQueryForms = new TreeSet<>();
    }

    public Map<String, UserRole> getDeserializedUserRoles(Map<String, QueryForm> queryFormMap) {
        Map<String, UserRole> deserializedUserRoles = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        definedUserRoles.forEach(serializedUserRole -> {
            UserRole deserializedUserRole = serializedUserRole.deserialize(queryFormMap);
            deserializedUserRoles.put(deserializedUserRole.getId(), deserializedUserRole);
        });

        return deserializedUserRoles;
    }

    public Map<String, QueryForm> getDeserializedQueryForms() {
        Map<String, QueryForm> deserializedQueryForms = new HashMap<>();

        definedQueryForms.forEach(serializedQueryForm -> {
            QueryForm deserilizedQueryForm = serializedQueryForm.deserialize();
            deserializedQueryForms.put(deserilizedQueryForm.getId(), deserilizedQueryForm);
        });

        return deserializedQueryForms;
    }
}
