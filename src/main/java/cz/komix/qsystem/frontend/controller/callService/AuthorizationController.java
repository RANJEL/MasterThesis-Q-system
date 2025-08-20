package cz.komix.qsystem.frontend.controller.callService;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.logic.role.UserRole;
import cz.komix.qsystem.backend.security.user.LoggedUser;
import cz.komix.qsystem.backend.service.AuthorizationService;
import cz.komix.qsystem.frontend.controller.messages.MessagesController;

import org.primefaces.context.RequestContext;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Jan Lejnar
 */
@Named
@ApplicationScoped
public class AuthorizationController {

    private AuthorizationService authorizationService;
    private MessagesController messagesController;

    @Inject
    public AuthorizationController(AuthorizationService authorizationService, MessagesController messagesController) {
        this.authorizationService = authorizationService;
        this.messagesController = messagesController;
    }

    // Query Forms
    private boolean isQueryFormValid(String queryFormName) {
        boolean result = true;
        if (queryFormName.isEmpty()) {
            result = false;
        }
        return result;
    }

    public boolean isQueryFormNameUnique(String queryFormName) {
        return authorizationService.isQueryFormNameUnique(queryFormName);
    }

    public void addQueryForm(String queryFormName, String queryFormDetail) {
        if (isQueryFormValid(queryFormName)) {
            if (isQueryFormNameUnique(queryFormName)) {
                authorizationService.addQueryForm(queryFormName, queryFormDetail);
                messagesController.growlMessage(
                        FacesMessage.SEVERITY_INFO, "",
                        "generic.message.queryFormSavedSuccess"
                );
            } else {
                messagesController.growlMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "generic.label.error",
                        "generic.message.enterDifferentName"
                );
            }
        } else {
            messagesController.growlMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "generic.label.error",
                    "generic.message.enterName"
            );
        }
    }

    public List<QueryForm> getAllQueryForms() {
        return authorizationService.getAllQueryForms();
    }

    public QueryForm getQueryForm(String queryFormId) {
        return authorizationService.getQueryForm(queryFormId);
    }

    public void deleteQueryForm(String queryFormName) {
        authorizationService.deleteQueryForm(queryFormName);
        RequestContext.getCurrentInstance().update("queryFormsList");
        messagesController.growlMessage(
                FacesMessage.SEVERITY_INFO, "",
                "generic.message.queryFormDeletedSuccess"
        );
    }

    public void updateQueryForm(QueryForm queryForm) {
        authorizationService.updateQueryForm(queryForm);
        messagesController.growlMessage(
                FacesMessage.SEVERITY_INFO, "",
                "generic.message.queryFormUpdatedSuccess"
        );
    }
    // /Query Forms

    // User Roles
    private boolean isUserRoleValid(String userRoleName) {
        boolean result = true;
        if (userRoleName.isEmpty()) {
            result = false;
        }
        return result;
    }

    public boolean isUserRoleNameUnique(String userRoleName) {
        return authorizationService.isUserRoleNameUnique(userRoleName);
    }

    public void addUserRole(String userRole, String userRoleDetail) {
        if (isUserRoleValid(userRole)) {
            if (isUserRoleNameUnique(userRole)) {
                authorizationService.addUserRole(userRole, userRoleDetail);
                messagesController.growlMessage(
                        FacesMessage.SEVERITY_INFO, "",
                        "generic.message.userRoleSavedSuccess"
                );
            } else {
                messagesController.growlMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "generic.label.error",
                        "generic.message.enterDifferentName"
                );
            }
        } else {
            messagesController.growlMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "generic.label.error",
                    "generic.message.enterName"
            );
        }
    }

    public List<UserRole> getAllUserRoles() {
        return authorizationService.getAllUserRoles();
    }

    public UserRole getUserRole(String userRoleId) {
        return authorizationService.getUserRole(userRoleId);
    }

    public void deleteUserRole(String userRole) {
        authorizationService.deleteUserRole(userRole);
        messagesController.growlMessage(
                FacesMessage.SEVERITY_INFO, "",
                "generic.message.userRoleDeletedSuccess"
        );
    }

    public void updateUserRole(UserRole userRole) {
        authorizationService.updateUserRole(userRole);
        messagesController.growlMessage(
                FacesMessage.SEVERITY_INFO, "",
                "generic.message.userRoleUpdatedSuccess"
        );
    }
    // /User Roles

    public void logEntryIntoAdministration(String pageName) {
        String userName = getLoggedUser().getName();
        String adminBookmark;

        ResourceBundle resourceBundle = ResourceBundle.getBundle("cz.komix.qsystem.frontend.messages.messages");
        adminBookmark = resourceBundle.getString("menu.administration." + pageName);
        if (adminBookmark == null) {
            adminBookmark = pageName;
        }

        authorizationService.logEntryIntoAdministration(userName, adminBookmark);
    }

    public LoggedUser getLoggedUser() {
        return authorizationService.getLoggedUser();
    } // this is @SessionScoped

}
