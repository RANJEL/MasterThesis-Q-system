package cz.komix.qsystem.backend.logic.usecases.fewrapper;

import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbQueryEntityType;

/**
 * FE wrapper of {@link JaxbQueryEntityType} that adds info if that entity could be selected based on user roles.
 *
 * @author Jan Lejnar
 */
public class FEQueryEntityType extends JaxbQueryEntityType {
    private boolean isSelectable;

    public FEQueryEntityType(JaxbQueryEntityType jaxbQueryEntityType) {
        this.setName(jaxbQueryEntityType.getName());
        this.setElemOrder(jaxbQueryEntityType.getElemOrder());
        this.setAllowedSearchCriteriaCombinations(jaxbQueryEntityType.getAllowedSearchCriteriaCombinations());
        this.setSearchCriteriaMap(jaxbQueryEntityType.getSearchCriteriaMap());
        this.isSelectable = true;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }
}
