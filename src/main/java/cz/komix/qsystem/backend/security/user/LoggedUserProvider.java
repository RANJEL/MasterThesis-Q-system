package cz.komix.qsystem.backend.security.user;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.logic.role.UserRole;
import cz.komix.qsystem.backend.persistence.config.AbstractConfigurationProvider;
import cz.komix.qsystem.backend.persistence.config.part.authorization.IAuthorizationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * Session bean that could be injected from everywhere and provides user that has logged in current session.
 *
 * @author Jan Lejnar
 */
@SessionScoped
@Named
public class LoggedUserProvider implements Serializable {

    private final transient IAuthorizationConfig authorizationConfig;
    private final transient Logger logger = LoggerFactory.getLogger(LoggedUserProvider.class);
    private LoggedUser loggedUser;

    @Inject
    public LoggedUserProvider(
            @Named("tomlConfigurationProvider") AbstractConfigurationProvider configuration) {
        this.authorizationConfig = configuration.getAuthorizationConfiguration();
    }

    private boolean wasUserAlreadyLogged() {
        if (loggedUser == null) {
            return false;
        }

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return loggedUser.getName().equals(userName);
    }

    private String trimUserRole(String userRole) {
        return userRole.replaceFirst("ROLE_", "");
    }

    private Set<UserRole> getAllUserRolesOfLoggedUser(
            Authentication springAuth,
            List<String> knownUserRoles,
            List<String> unknownUserRoles) {
        Set<UserRole> allUserRolesOfLoggedUser = new HashSet<>();

        Map<String, UserRole> qSystemUserRoles = authorizationConfig.getUserRoles();
        // TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        springAuth.getAuthorities().forEach(
                userRole -> {
                    String qSystemUserRole = trimUserRole(userRole.getAuthority());
                    UserRole matchingUserRole = qSystemUserRoles.get(qSystemUserRole); // ignore case
                    if (matchingUserRole == null) {
                        unknownUserRoles.add(qSystemUserRole);
                    } else {
                        knownUserRoles.add(qSystemUserRole);
                        allUserRolesOfLoggedUser.add(matchingUserRole);
                    }
                });

        if (!unknownUserRoles.isEmpty()) { // UC PP0401 step 9a
            unknownUserRoles.forEach(
                    unknownUserRole -> logger.warn(
                            "User {} has assigned user role {} that is unknown for this system. In order to use this role make sure that administrator firstly adds this user role into system through administration.",
                            springAuth.getName(),
                            unknownUserRole)
            );
        }

        return allUserRolesOfLoggedUser;
    }

    private Set<QueryForm> getAllAllowedQueryFormsOfLoggedUser(Set<UserRole> allUserRolesOfLoggedUser) {
        Set<QueryForm> allAllowedQueryFormsOfLoggedUser = new HashSet<>();

        allUserRolesOfLoggedUser.forEach(
                userRole -> allAllowedQueryFormsOfLoggedUser.add(userRole.getQueryFormIntersection())
        );

        return allAllowedQueryFormsOfLoggedUser;
    }

    private void createLoggedUserContext() {
        Authentication springAuth = SecurityContextHolder.getContext().getAuthentication();
        List<String> knownUserRoles = new ArrayList<>();
        List<String> unknownUserRoles = new ArrayList<>();
        Set<UserRole> allUserRolesOfLoggedUser = getAllUserRolesOfLoggedUser(springAuth, knownUserRoles, unknownUserRoles);
        Set<QueryForm> allAllowedQueryFormsOfLoggedUser = getAllAllowedQueryFormsOfLoggedUser(allUserRolesOfLoggedUser);
        QueryForm loggedUserQueryFormIntersection = QueryForm.mergeQueryForms(allAllowedQueryFormsOfLoggedUser.toArray(QueryForm[]::new));
        loggedUser = new LoggedUser(
                springAuth.getName(),
                springAuth.getAuthorities(),
                knownUserRoles,
                unknownUserRoles,
                loggedUserQueryFormIntersection);
    }

    public LoggedUser getLoggedUser() {
        if (!wasUserAlreadyLogged()) {
            createLoggedUserContext();
            logger.info(
                    "User {} has been successfully authenticated and authorized",
                    loggedUser.getName());
        }

        return loggedUser;
    }
}
