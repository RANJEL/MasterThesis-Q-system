package cz.komix.qsystem.frontend.controller.admin.userRoles;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.logic.role.UserRole;
import cz.komix.qsystem.frontend.controller.admin.userRoles.fewrapper.FeQueryFormWrapper;
import cz.komix.qsystem.frontend.controller.callService.AuthorizationController;
import cz.komix.qsystem.frontend.controller.messages.MessagesController;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static cz.komix.qsystem.frontend.searchcriteria.categorygenerators.AbstractCategoryEntityFactory.isValidParamValue;

/**
 * @author Jan Lejnar
 */
@Named
@ViewScoped
public class UserRolesDetailController {

    private AuthorizationController authorizationController;
    private MessagesController messagesController;

    private UserRole userRoleBeforeModification;
    private Map<String, FeQueryFormWrapper> queryForms = new HashMap<>();

    @Inject
    public UserRolesDetailController(AuthorizationController authorizationController, MessagesController messagesController) {
        this.authorizationController = authorizationController;
        String userRoleId = ((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getParameter("user_role_id");
        this.userRoleBeforeModification = authorizationController.getUserRole(userRoleId);
        if (this.userRoleBeforeModification == null) {
            this.userRoleBeforeModification = new UserRole("", "");
        }

        Set<QueryForm> userRoleAllowsQFs = this.userRoleBeforeModification.getAllowedQueryForms();
        authorizationController.getAllQueryForms().forEach(
                definedQueryForm -> queryForms.put(definedQueryForm.getName(), new FeQueryFormWrapper(
                        definedQueryForm,
                        userRoleAllowsQFs.contains(definedQueryForm))
                )
        );

    }

    public UserRole getUserRoleBeforeModification() {
        return userRoleBeforeModification;
    }

    public Map<String, FeQueryFormWrapper> getQueryForms() {
        return queryForms;
    }

    public String saveUserRole() {
        UserRole userRole = new UserRole(userRoleBeforeModification.getName(), userRoleBeforeModification.getDescription());
        queryForms.values().stream()
                .filter(FeQueryFormWrapper::isChecked)
                .forEach(feQueryFormWrapper -> userRole.getAllowedQueryForms().add(feQueryFormWrapper.getQueryForm()));
        userRole.updateQueryFormIntersection();

        if (isValidParamValue(userRole.getName())) {
            authorizationController.updateUserRole(userRole);
        }
        return "main.xhtml?faces-redirect=true";
    }
}
