package cz.komix.qsystem.frontend.controller.admin.userRoles.fewrapper;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;

/**
 * @author Jan Lejnar
 */
public class FeQueryFormWrapper {
    private QueryForm queryForm;
    private boolean checked;

    public FeQueryFormWrapper(QueryForm queryForm, boolean checked) {
        this.queryForm = queryForm;
        this.checked = checked;
    }

    public QueryForm getQueryForm() {
        return queryForm;
    }

    public void setQueryForm(QueryForm queryForm) {
        this.queryForm = queryForm;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
