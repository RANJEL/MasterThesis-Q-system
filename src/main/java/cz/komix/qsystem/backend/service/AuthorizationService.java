package cz.komix.qsystem.backend.service;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.logic.role.UserRole;
import cz.komix.qsystem.backend.logic.usecases.IQSystemUseCases;
import cz.komix.qsystem.backend.persistence.config.AbstractConfigurationProvider;
import cz.komix.qsystem.backend.persistence.config.part.authorization.IAuthorizationConfig;
import cz.komix.qsystem.backend.security.user.LoggedUser;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * List of methods dealing with authorization provided by backend.
 *
 * @author Jan Lejnar
 */
@RestController
@Named
public class AuthorizationService {

    private IAuthorizationConfig authorizationConfig;
    private IQSystemUseCases useCases;

    @Inject
    public AuthorizationService(AbstractConfigurationProvider configuration, IQSystemUseCases useCases) {
        this.authorizationConfig = configuration.getAuthorizationConfiguration();
        this.useCases = useCases;
    }

    /**
     * UC PP0201 step 5
     *
     * @param queryFormName
     * @return
     */
    public boolean isQueryFormNameUnique(String queryFormName) {
        return authorizationConfig.isQueryFormNameUnique(queryFormName);
    }

    /**
     * UC PP0201 steps 6-7
     *
     * @param queryFormName
     * @param queryFormDetail
     */
    public void addQueryForm(String queryFormName, String queryFormDetail) {
        authorizationConfig.addQueryForm(new QueryForm(queryFormName, queryFormDetail));
    }

    /**
     * Screen O05
     *
     * @return
     */
    public List<QueryForm> getAllQueryForms() {
        return authorizationConfig.getQueryFormsSortedByName();
    }

    /**
     * Screen O06
     *
     * @param queryFormId id of query form
     * @return
     */
    public QueryForm getQueryForm(String queryFormId) {
        return authorizationConfig.getQueryForms().get(queryFormId);
    }

    /**
     * Screen O05
     *
     * @param queryFormName
     */
    public void deleteQueryForm(String queryFormName) {
        authorizationConfig.removeQueryForm(queryFormName);
    }

    /**
     * Screen O06
     *
     * @param queryForm
     */
    public synchronized void updateQueryForm(QueryForm queryForm) {
        authorizationConfig.updateQueryForm(queryForm);
    }

    /**
     * UC PP0202 step 5
     *
     * @param userRoleName
     * @return
     */
    public boolean isUserRoleNameUnique(String userRoleName) {
        return authorizationConfig.isUserRoleNameUnique(userRoleName);
    }

    /**
     * UC PP0202 steps 6-7
     *
     * @param userRole
     * @param userRoleDetail
     */
    public void addUserRole(String userRole, String userRoleDetail) {
        authorizationConfig.addUserRole(new UserRole(userRole, userRoleDetail));
    }

    /**
     * Screen O07
     *
     * @return
     */
    public List<UserRole> getAllUserRoles() {
        return authorizationConfig.getUserRolesSortedByName();
    }

    /**
     * Screen O08
     *
     * @param userRoleId id of user role
     * @return
     */
    public UserRole getUserRole(String userRoleId) {
        return authorizationConfig.getUserRoles().get(userRoleId);
    }

    /**
     * Screen O07
     *
     * @param userRole
     */
    public void deleteUserRole(String userRole) {
        authorizationConfig.removeUserRole(userRole);
    }

    /**
     * Screen O08, UC PP0203 steps 7-8
     *
     * @param userRole
     */
    public synchronized void updateUserRole(UserRole userRole) {
        authorizationConfig.updateUserRole(userRole);
    }

    public void logEntryIntoAdministration(String userName, String adminBookmark) {
        useCases.logEntryIntoAdministration(userName, adminBookmark);
    }

    /**
     * O01
     *
     * @return
     */
    public LoggedUser getLoggedUser() {
        return useCases.getLoggedUser();
    }
}
