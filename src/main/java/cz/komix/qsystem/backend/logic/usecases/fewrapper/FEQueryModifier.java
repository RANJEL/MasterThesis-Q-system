package cz.komix.qsystem.backend.logic.usecases.fewrapper;

import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbQueryModifier;

/**
 * FE wrapper of {@link JaxbQueryModifier} that adds info if that query modifier could be checked
 * based on user roles and allowed query forms.
 *
 * @author Jan Lejnar
 */
public class FEQueryModifier extends JaxbQueryModifier {
    private boolean isCheckable;

    public FEQueryModifier(JaxbQueryModifier jaxbQueryModifier) {
        this.setName(jaxbQueryModifier.getName());
        this.setElemOrder(jaxbQueryModifier.getElemOrder());
        this.isCheckable = true;
    }

    public boolean isCheckable() {
        return isCheckable;
    }

    public void setCheckable(boolean checkable) {
        isCheckable = checkable;
    }
}
