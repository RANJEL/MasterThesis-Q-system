package cz.komix.qsystem.backend.security.user;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.persistence.config.serializationtoml.authorization.SerializedQueryForm;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Representation of user that stores additional information about him, e.g. based on assigned user roles his query form intersection.
 *
 * @author Jan Lejnar
 */
public class LoggedUser implements Serializable {
    private String name;
    private Collection<? extends GrantedAuthority> ldapUserRoles;
    private List<String> knownUserRoles;
    private List<String> unknownUserRoles;
    private SerializedQueryForm loggedUserQueryFormIntersectionSerialized;
    private boolean isAdmin;

    /**
     * UC PP0401, steps 9-10
     *
     * @param name
     * @param ldapUserRoles
     */
    public LoggedUser(
            String name,
            Collection<? extends GrantedAuthority> ldapUserRoles,
            List<String> knownUserRoles,
            List<String> unknownUserRoles,
            QueryForm loggedUserQueryFormIntersection) {
        this.name = name;
        this.ldapUserRoles = ldapUserRoles;
        this.knownUserRoles = knownUserRoles;
        this.unknownUserRoles = unknownUserRoles;
        this.loggedUserQueryFormIntersectionSerialized = loggedUserQueryFormIntersection.serialize();
        this.isAdmin = knownUserRoles.stream().
                anyMatch(userRole -> userRole.equalsIgnoreCase("ADMIN"));
    }

    public String getName() {
        return name;
    }

    public Collection<? extends GrantedAuthority> getLdapUserRoles() {
        return ldapUserRoles;
    }

    public List<String> getKnownUserRoles() {
        return knownUserRoles;
    }

    public List<String> getUnknownUserRoles() {
        return unknownUserRoles;
    }

    public QueryForm getLoggedUserQueryFormIntersection() {
        return loggedUserQueryFormIntersectionSerialized.deserialize();
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
