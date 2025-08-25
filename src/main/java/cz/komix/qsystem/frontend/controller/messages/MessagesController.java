package cz.komix.qsystem.frontend.controller.messages;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.ResourceBundle;

@Named
@ApplicationScoped
public class MessagesController {

    private boolean menuShow = true;
    private String menuLabel = "menu.showButton.hide";

    public boolean getMenuShow() {
        return this.menuShow;
    }

    public String getMenuLabel() {
        return this.menuLabel;
    }

    public void actionMenuShow() {
        if (this.getMenuShow() == true) {
            this.menuShow = false;
            this.menuLabel = "menu.showButton.show";
        } else {
            this.menuShow = true;
            this.menuLabel = "menu.showButton.hide";
        }
    }

    private FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    private ResourceBundle getResourceBundle() {
        return getFacesContext().getApplication().getResourceBundle(getFacesContext(), "msg");
    }

    public String getResourceMessage(String key) {
        return getResourceBundle().getString(key);
    }

    public void growlMessage(FacesMessage.Severity severityLevel, String summary, String detail) {
        getFacesContext().addMessage(
                null,
                new FacesMessage(
                        severityLevel,
                        ((summary != null && !summary.isEmpty()) ? getResourceMessage(summary) : ""),
                        ((detail != null && !detail.isEmpty()) ? getResourceMessage(detail) : "")
                )
        );
    }

}
