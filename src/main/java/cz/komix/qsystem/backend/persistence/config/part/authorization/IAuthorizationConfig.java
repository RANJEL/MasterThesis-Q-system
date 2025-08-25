package cz.komix.qsystem.backend.persistence.config.part.authorization;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.logic.role.UserRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * All methods we need for authorization.
 *
 * @author Jan Lejnar
 */
public interface IAuthorizationConfig {
    void loadAuthorizationConfig();

    void saveAuthorizationConfig();

    Map<String, QueryForm> getQueryForms();

    default List<QueryForm> getQueryFormsSortedByName() {
        List<QueryForm> list = new ArrayList<>(getQueryForms().values());
        Collections.sort(list);
        return list;
    }

    void addQueryForm(QueryForm queryForm);

    void removeQueryForm(String queryFormName);

    void updateQueryForm(QueryForm queryForm);

    Map<String, UserRole> getUserRoles();

    default List<UserRole> getUserRolesSortedByName() {
        List<UserRole> list = new ArrayList<>(getUserRoles().values());
        Collections.sort(list);
        return list;
    }

    void addUserRole(UserRole userRole);

    void removeUserRole(String userRoleName);

    void updateUserRole(UserRole userRole);

    default boolean isQueryFormNameUnique(String queryFormName) {
        return getQueryForms().values().stream()
                .noneMatch(queryForm -> queryForm.getName().equals(queryFormName));
    }

    default boolean isUserRoleNameUnique(String userRoleName) {
        return getUserRoles().values().stream()
                .noneMatch(userRole -> userRole.getName().equals(userRoleName));
    }
}
