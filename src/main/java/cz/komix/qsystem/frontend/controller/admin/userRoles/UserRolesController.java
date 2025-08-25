package cz.komix.qsystem.frontend.controller.admin.userRoles;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * @author Jan Lejnar
 */
@Named
@ViewScoped
public class UserRolesController {

    private String userRoleName;
    private String userRoleDescription;

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public String getUserRoleDescription() {
        return userRoleDescription;
    }

    public void setUserRoleDescription(String userRoleDescription) {
        this.userRoleDescription = userRoleDescription;
    }

    public String viewUserRoleDetail(String userRoleId) {
        return "detail.xhtml?faces-redirect=true&user_role_id=" + userRoleId;
    }
}
