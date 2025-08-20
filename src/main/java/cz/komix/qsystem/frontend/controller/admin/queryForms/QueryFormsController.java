package cz.komix.qsystem.frontend.controller.admin.queryForms;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * @author Jan Lejnar
 */
@Named
@ViewScoped
public class QueryFormsController {

    private String queryFormName;
    private String queryFormDescription;

    public String getQueryFormName() {
        return queryFormName;
    }

    public void setQueryFormName(String queryFormName) {
        this.queryFormName = queryFormName;
    }

    public String getQueryFormDescription() {
        return queryFormDescription;
    }

    public void setQueryFormDescription(String queryFormDescription) {
        this.queryFormDescription = queryFormDescription;
    }

    public String viewQueryFormDetail(String queryFormId) {
        return "detail.xhtml?faces-redirect=true&query_form_id=" + queryFormId;
    }
}
